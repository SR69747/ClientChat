package listeners;

import gui.Chat;
import networking.Sender;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.*;

public final class DropListener implements DropTargetListener, Serializable {
    /**
     * Called while a drag operation is ongoing, when the mouse pointer enters
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.
     *
     * @param dtde the <code>DropTargetDragEvent</code>
     */
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        System.out.println("Drag enter !");
    }

    /**
     * Called when a drag operation is ongoing, while the mouse pointer is still
     * over the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.
     *
     * @param dtde the <code>DropTargetDragEvent</code>
     */
    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        System.out.println("Drag over !");
    }

    /**
     * Called if the user has modified
     * the current drop gesture.
     * <p>
     *
     * @param dtde the <code>DropTargetDragEvent</code>
     */
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        System.out.println("Drop change !");
    }

    /**
     * Called while a drag operation is ongoing, when the mouse pointer has
     * exited the operable part of the drop site for the
     * <code>DropTarget</code> registered with this listener.
     *
     * @param dte the <code>DropTargetEvent</code>
     */
    @Override
    public void dragExit(DropTargetEvent dte) {
        System.out.println("Drag exit !");
    }

    /**
     * Called when the drag operation has terminated with a drop on
     * the operable part of the drop site for the <code>DropTarget</code>
     * registered with this listener.
     * <p>
     * This method is responsible for undertaking
     * the transfer of the data associated with the
     * gesture. The <code>DropTargetDropEvent</code>
     * provides a means to obtain a <code>Transferable</code>
     * object that represents the data object(s) to
     * be transfered.<P>
     * From this method, the <code>DropTargetListener</code>
     * shall accept or reject the drop via the
     * acceptDrop(int dropAction) or rejectDrop() methods of the
     * <code>DropTargetDropEvent</code> parameter.
     * <p>
     * Subsequent to acceptDrop(), but not before,
     * <code>DropTargetDropEvent</code>'s getTransferable()
     * method may be invoked, and data transfer may be
     * performed via the returned <code>Transferable</code>'s
     * getTransferData() method.
     * <p>
     * At the completion of a drop, an implementation
     * of this method is required to signal the success/failure
     * of the drop by passing an appropriate
     * <code>boolean</code> to the <code>DropTargetDropEvent</code>'s
     * dropComplete(boolean success) method.
     * <p>
     *
     * @param dtde the <code>DropTargetDropEvent</code>
     */
    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable t = dtde.getTransferable();
            if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                dtde.acceptDrop(dtde.getDropAction());
                String s = t.getTransferData(DataFlavor.stringFlavor).toString();
                Sender.sendMessageToServer(s);
                dtde.dropComplete(true);
            } else if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(dtde.getDropAction());
                Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                if (o != null) {
                    String path = o.toString().substring(1, o.toString().length() - 1);
                    File file = new File(path);
                    if (path.contains(".jpg") || path.contains(".png") || path.contains(".gif")) {
                        if (file.length() < 1000000L) {
                            Chat.displayMessageInHTML("Image sent!", "orange", true);
                            Sender.sendImageToServer(file);
                        } else {
                            Chat.displayMessageInHTML("Error: File is too big", "red", false);
                        }
                    } else if (path.contains(".txt")) {

                        if (file.length() < 1000000L) {
                            String fileData = readFile(file);
                            Sender.sendMessageToServer(fileData);
                        } else {
                            Chat.displayMessageInHTML("Error: File is too big", "red", false);
                        }
                    } else {
                        Chat.displayMessageInHTML("Error: Unknown File", "red", false);
                    }
                }
                dtde.dropComplete(true);
            } else {
                dtde.rejectDrop();
            }
        } catch (java.io.IOException | UnsupportedFlavorException e2) {
            System.out.println(e2.getMessage());
        }
    }

    private static String readFile(File file) {
        StringBuilder fileText = new StringBuilder(5);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                fileText.append(currentLine).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileText.toString();
    }

}
