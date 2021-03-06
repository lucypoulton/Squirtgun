package net.lucypoulton.squirtgun.platform.event.cancellable;

public abstract class AbstractCancellableEvent implements CancellableEvent {
    private boolean isCancelled;

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
}
