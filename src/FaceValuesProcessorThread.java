import java.util.*;
import java.util.concurrent.*;

public class FaceValuesProcessorThread extends Thread implements FaceValuesListener {

    private ArrayBlockingQueue<KissEventListener> kissEventListeners = new ArrayBlockingQueue<>(11);

    private Map<String, LinkedBlockingQueue<Float>> clientsToFaceValuesMap = new ConcurrentHashMap<>();

    @Override
    public void onNewValues(float cos, String clientId) {
            LinkedBlockingQueue<Float> values = clientsToFaceValuesMap.get(clientId);
            if (values == null) {
                values = new LinkedBlockingQueue<>();
            }
            if (values.size() <= 10) {
                values.add(cos);
            } else {
                values.poll();
                values.add(cos);
            }

            clientsToFaceValuesMap.put(clientId, values);
    }

    @Override
    public void run() {
        while (true) {
            for (Map.Entry<String, LinkedBlockingQueue<Float>> entry : clientsToFaceValuesMap.entrySet()) {
                LinkedBlockingQueue<Float> values = entry.getValue();
                // System.out.println("Blocking Queue: " + values);
                boolean shouldSend = values.stream().filter(it -> it < -0.15f).count() > 8;
                for (KissEventListener kissEventListener : kissEventListeners) {
                    System.out.println(kissEventListener.clientId);
                }

                if (shouldSend) {
                    System.out.println("---------------------------------------------");
                }
                if (shouldSend) {
                    for (KissEventListener kissEventListener : kissEventListeners) {
                       //if (!kissEventListener.clientId.equals(entry.getKey()))
                            kissEventListener.onSmileEvent();
                    }
                }
            }
        }
    }

    @Override
    public void addKissEventListener(KissEventListener kissEventListener) {
        kissEventListeners.add(kissEventListener);
    }

    @Override
    public void onKissValue(String clientId) {
            for (KissEventListener kissEventListener : kissEventListeners) {
                //if (!kissEventListener.clientId.equals(clientId))
                    kissEventListener.onKissEvent();
            }
    }
}
