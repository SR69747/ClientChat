package listeners;

import gui.Chat;
import networking.Sender;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.*;
import java.util.Base64;

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

            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(dtde.getDropAction());
                Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                if (o != null) {
                    String path = o.toString().substring(1, o.toString().length() - 1);
                    if (path.contains(".jpg") || path.contains(".png")) {
                        //TODO Simplify, move it to the method in Sender class
                        if (!Sender.getSelectedUserName().isEmpty()) {
                            Sender.sendMessageToServer(String.format("\u0002@\u0003%s\u0003%s\n", Sender.getSelectedUserName().trim(), "\u0002#" + encodeFileToBase64Binary(new File(path))));
                        } else {
                            Sender.sendMessageToServer("\u0002#" + encodeFileToBase64Binary(new File(path)));
                        }
                    }else {
                        Chat.displayMessageInHTML("Error: Unknown File","red",false);
                    }
                }
                dtde.dropComplete(true);
            }
        } catch (java.io.IOException | UnsupportedFlavorException e2) {
            System.out.println(e2.getMessage());
        }
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedFile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedFile = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedFile;
    }

//    private static byte[] serialize(Object obj) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ObjectOutputStream os = new ObjectOutputStream(out);
//        os.writeObject(obj);
//        return out.toByteArray();
//    }
}
