package de.webspired.Client;

import de.webspired.GreenfootNetworking.NetworkedActor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActorHandler extends Thread {


    private final Client c;
    private final HashMap<Integer, NetworkedActor> networkedActorsToAdd = new HashMap<>();
    private final HashMap<Integer, NetworkedActor> networkedActorsToRemove = new HashMap<>();

    public ActorHandler(Client c) {
        this.c = c;
        this.start();
    }

    public void scheduleAddActor(int id) {
        synchronized (networkedActorsToAdd) {
            this.networkedActorsToAdd.put(id, c.getActor(id));
        }
    }

    public void scheduleRemoveActor(int id) {
        synchronized (networkedActorsToRemove) {
            this.networkedActorsToRemove.put(id, c.getActor(id));
        }
    }

    @Override
    public void run() {
        ArrayList<Integer> indexesToRemoveFromAdd = new ArrayList<>();
        ArrayList<Integer> indexesToRemoveFromRemove = new ArrayList<>();
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (networkedActorsToAdd) {
                for (Map.Entry<Integer, NetworkedActor> entry : this.networkedActorsToAdd.entrySet()) {
                    if(entry.getValue() == null){
                        indexesToRemoveFromAdd.add(entry.getKey());
                        continue;
                    }
                    if (!entry.getValue().isAddableToWorld()) continue;
                    c.informToAdd(entry.getValue());
                    indexesToRemoveFromAdd.add(entry.getKey());

                }
            }
            synchronized (networkedActorsToRemove) {
                for (Map.Entry<Integer, NetworkedActor> entry : this.networkedActorsToRemove.entrySet()) {
                    if(entry.getValue() == null){
                        indexesToRemoveFromRemove.add(entry.getKey());
                        continue;
                    }
                    if (entry.getValue().getWorldId() == -1) continue;
                    c.informToRemove(entry.getValue());
                    indexesToRemoveFromRemove.add(entry.getKey());
                }
            }

            for (int i : indexesToRemoveFromAdd) {
                this.networkedActorsToAdd.remove(i);
            }
            for (int i : indexesToRemoveFromRemove) {
                this.networkedActorsToRemove.remove(i);
            }
            indexesToRemoveFromAdd.clear();
            indexesToRemoveFromRemove.clear();
        }
    }
}
