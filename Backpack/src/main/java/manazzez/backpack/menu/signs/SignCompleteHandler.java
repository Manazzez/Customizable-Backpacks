package manazzez.backpack.menu.signs;

@FunctionalInterface
public interface SignCompleteHandler {
    void onSignClose(SignCompletedEvent event);
}

