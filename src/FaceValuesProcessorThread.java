import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceValuesProcessorThread extends Thread implements FaceValuesListener {

    private List<KissEventListener> kissEventListeners = new ArrayList<>();

    private Map<String, Pair<Float, Float>> clientsToFaceValuesMap = new HashMap<>();

    @Override
    public void onNewValues(float width, float height, String clientId) {
        clientsToFaceValuesMap.put(clientId, new Pair<Float, Float>(Float.valueOf(width), Float.valueOf(height)));
    }

    @Override
    public void run() {
        while (true) {
            for (Map.Entry<String, Pair<Float, Float>> entry : clientsToFaceValuesMap.entrySet()) {
               Pair<Float, Float> pair = entry.getValue();
               float width = pair.getKey();
               float height = pair.getValue();

                if (width > 1000f) {
                    for (KissEventListener kissEventListener : kissEventListeners) {
                        if (kissEventListener.clientId.equals(entry.getKey())) {
                            continue;
                        } else {
                            kissEventListener.onKissEvent();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addKissEventListener(KissEventListener kissEventListener) {
        kissEventListeners.add(kissEventListener);
    }
}
