public interface FaceValuesListener {
    void onNewValues(float width, float height, String clientId);

    void addKissEventListener(KissEventListener kissEventListener);
}
