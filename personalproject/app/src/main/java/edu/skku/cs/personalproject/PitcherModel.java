package edu.skku.cs.personalproject;

import androidx.annotation.NonNull;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PitcherModel implements Contract.ContractForHitterModel {
    private String position;
    private int deleted_id;
    private int candidates_num;
    private int[] playersIdList;
    private ArrayList<Integer> candidatesIdList;
    private HashMap<String, Integer> map;

    public PitcherModel() {
        candidates_num = 0;
        playersIdList = new int[5];
        for (int i = 0; i < 5; i++) playersIdList[i] = -1;
        candidatesIdList = new ArrayList<>();
        map = new HashMap<>();
        map.put("P1", 0);
        map.put("P2", 1);
        map.put("P3", 2);
        map.put("P4", 3);
        map.put("P5", 4);
    }

    @Override
    public String getPosition() {
        return position;
    }

    @Override
    public int getDeletedId() {
        return deleted_id;
    }

    @Override
    public void setPosition(String position, OnValueChangedListener listener) {
        this.position = position;
        listener.onChanged();
    }

    @Override
    public void setValue(String position, int id, OnValueChangedListener listener) {
        if (candidates_num >= 7 && id == -1) return;
        this.position = position;
        deleted_id = id;
        listener.onChanged();
    }

    @Override
    public void addId(int id, OnValueChangedListener listener) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("~/add").newBuilder(); // ~/add
        String url = urlBuilder.build().toString();

        JSONObject json = new JSONObject();
        json.put("player_id", id);
        json.put("position", "P");
        json.put("type", "Pitcher");
        Request req = new Request.Builder().url(url).post(RequestBody.create(json.toString(), MediaType.parse("application/json"))).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                try { jsonObject = (JSONObject) parser.parse(myResponse); }
                catch (ParseException e) { e.printStackTrace(); }
                boolean success = (boolean) jsonObject.get("success");
                if (success) {
                    candidatesIdList.add(id);
                    candidates_num++;
                    listener.onCandidatesIdListChanged(candidates_num, candidatesIdList);
                }
                else listener.onFail();
            }
        });
    }

    @Override
    public void changeId(int id, OnValueChangedListener listener) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("~/update").newBuilder(); // ~/update
        String url = urlBuilder.build().toString();

        JSONObject json = new JSONObject();
        json.put("prev", deleted_id);
        json.put("player_id", id);
        Request req = new Request.Builder().url(url).post(RequestBody.create(json.toString(), MediaType.parse("application/json"))).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                try { jsonObject = (JSONObject) parser.parse(myResponse); }
                catch (ParseException e) { e.printStackTrace(); }
                boolean success = (boolean) jsonObject.get("success");
                if (success) {
                    candidatesIdList.set(candidatesIdList.indexOf(deleted_id), id);
                    listener.onCandidatesIdListChanged(candidates_num, candidatesIdList);
                }
                else listener.onFail();
            }
        });
    }

    @Override
    public void resetIdList() {
        candidatesIdList = new ArrayList<>();
        candidates_num = 0;
        for (int i = 0; i < 5; i++) playersIdList[i] = -1;

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("~/reset").newBuilder(); // ~/reset
        String url = urlBuilder.build().toString();

        JSONObject json = new JSONObject();
        json.put("type", "Pitcher");
        Request req = new Request.Builder().url(url).post(RequestBody.create(json.toString(), MediaType.parse("application/json"))).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) { }
        });
    }

    @Override
    public void updatePlayer(int id, OnValueChangedListener listener) {
        OkHttpClient client = new OkHttpClient();
        String url;
        JSONObject json = new JSONObject();
        Integer idx = map.get(position);

        if (playersIdList[idx] == -1) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("~/add").newBuilder(); // ~/add
            url = urlBuilder.build().toString();

            json.put("player_id", id);
            json.put("position", position);
            json.put("type", "Pitcher");
        }
        else {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("~/update").newBuilder(); // ~/update
            url = urlBuilder.build().toString();

            json.put("prev", playersIdList[idx]);
            json.put("player_id", id);
        }

        Request req = new Request.Builder().url(url).post(RequestBody.create(json.toString(), MediaType.parse("application/json"))).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                try { jsonObject = (JSONObject) parser.parse(myResponse); }
                catch (ParseException e) { e.printStackTrace(); }
                boolean success = (boolean) jsonObject.get("success");
                if (success) {
                    playersIdList[idx] = id;
                    listener.onPlayersIdListChanged(position, id);
                }
                else listener.onFail();
            }
        });
    }

    @Override
    public void loadData(OnValueChangedListener listener) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("~/load").newBuilder(); // ~/load
        String url = urlBuilder.build().toString();

        JSONObject json = new JSONObject();
        json.put("type", "Pitcher");
        Request req = new Request.Builder().url(url).post(RequestBody.create(json.toString(), MediaType.parse("application/json"))).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                try { jsonObject = (JSONObject) parser.parse(myResponse); }
                catch (ParseException e) { e.printStackTrace(); }
                boolean success = (boolean) jsonObject.get("success");
                if (success == false) return;
                JSONArray players = (JSONArray) jsonObject.get("results");

                for(int i = 0; i < players.size(); i++) {
                    JSONObject player = (JSONObject) players.get(i);
                    int player_id = Integer.parseInt(String.valueOf(player.get("player_id")));
                    String position = (String) player.get("position");
                    if (!position.equals("P")) {
                        playersIdList[map.get(position)] = player_id;
                        listener.onPlayersIdListChanged(position, player_id);
                    }
                    else {
                        candidatesIdList.add(player_id);
                        candidates_num++;
                    }
                }

                listener.onCandidatesIdListChanged(candidates_num, candidatesIdList);
            }
        });
    }
}