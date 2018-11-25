public interface FaceValuesListener {
    void onNewValues(float cos, String clientId);

    void onKissValue(String clientId);

    void addKissEventListener(KissEventListener kissEventListener);
}
