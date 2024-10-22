package ubb.scs.map.service;


import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;

import java.util.*;

public class Comunitati {
    private final ServiceApp service;

    public Comunitati(ServiceApp service) {
        this.service = service;
    }

    private Map<Long, List<Long>> GrafPrietenii() {
        Map<Long, List<Long>> graf = new HashMap<>();

        for (Utilizator user : service.repoUsers.findAll()) {
            graf.putIfAbsent(user.getId(), new ArrayList<>());
        }
        for (Prietenie prietenie : service.repoPrietenie.findAll()) {
            graf.get(prietenie.getNodPrietenie1()).add(prietenie.getNodPrietenie2());
            graf.get(prietenie.getNodPrietenie2()).add(prietenie.getNodPrietenie1());
        }
        return graf;
    }

    private void DFS(Long userId, Set<Long> vizitat, Map<Long, List<Long>> graf, List<Long> componentaSocial) {
        vizitat.add(userId);
        componentaSocial.add(userId);
        for (Long prietenId : graf.get(userId)) {
            if (!vizitat.contains(prietenId)) {
                DFS(prietenId, vizitat, graf, componentaSocial);
            }
        }
    }

    public Long numarComunitati() {
        Map<Long, List<Long>> graf = GrafPrietenii();
        Set<Long> vizitat = new HashSet<>();
        long nrComunitati = 0L;
        for (Long userId : graf.keySet()) {
            if (!vizitat.contains(userId)) {
                DFS(userId, vizitat, graf, new ArrayList<>());
                nrComunitati += 1L;
            }
        }
        return nrComunitati;
    }

    private Long[] bfs(Long start, Map<Long, List<Long>> graf) {
        Queue<Long> queue = new LinkedList<>();
        queue.add(start);
        Map<Long, Integer> distanta = new HashMap<>();
        distanta.put(start, 0);
        Long distantat = start;
        int maxDistanta = 0;
        while (!queue.isEmpty()) {
            Long userCurent = queue.poll();
            int distantaCurenta = distanta.get(userCurent);
            for (Long vecinId : graf.get(userCurent)) {
                if (!distanta.containsKey(vecinId)) {
                    queue.add(vecinId);
                    distanta.put(vecinId, distantaCurenta + 1);
                }
                if (distanta.get(vecinId) > maxDistanta) {
                    maxDistanta = distanta.get(vecinId);
                    distantat = vecinId;
                }
            }
        }
        return new Long[]{distantat, (long) maxDistanta};
    }

    public List<Long> comunitateSociabila() {
        Map<Long, List<Long>> graf = GrafPrietenii();
        Set<Long> vizitat = new HashSet<>();
        int maxDrum = 0;
        List<Long> sociabil = new ArrayList<>();
        for (Long userId : graf.keySet()) {
            if (!vizitat.contains(userId)) {
                List<Long> compoentaSociala = new ArrayList<>();
                DFS(userId, vizitat, graf, compoentaSociala);

                Long startUser = compoentaSociala.get(0);
                Long[] BFSOne = bfs(startUser, graf);
                Long[] BFSTwo = bfs(BFSOne[0], graf);
                int lungime = BFSTwo[1].intValue();
                if (maxDrum < lungime) {
                    maxDrum = lungime;
                    sociabil = compoentaSociala;
                }
            }
        }
        return sociabil;
    }

}