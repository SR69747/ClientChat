package listeners;

import java.awt.dnd.*;
import java.io.Serializable;

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
        //TODO Implement.
    }

  /*  private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }*/
}