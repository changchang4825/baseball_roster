package edu.skku.cs.personalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerListModel implements Contract.ContractForPlayerListModel {
    private String team;
    private String year;
    private String position;
    private int idSize;
    private ArrayList<PlayerData> playerList;

    public PlayerListModel(String team, String year, String position) {
        this.team = team;
        this.year = year;
        this.position = position;
        playerList = new ArrayList<>();
    }

    @Override
    public boolean getIsHitter() {
        return !position.equals("P");
    }

    @Override
    public void sendRequest(OnValueChangedListener listener) {
        Log.d("@@@", position);
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://statsapi.mlb.com/api/v1/teams/" + team +"/roster/fullRoster").newBuilder();
        urlBuilder.addQueryParameter("season", year);

        String url = urlBuilder.build().toString();

        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String myResponse = response.body().string();

                JSONParser parser1 = new JSONParser();
                JSONObject jsonObject1 = null;
                try { jsonObject1 = (JSONObject) parser1.parse(myResponse); }
                catch (ParseException e) { e.printStackTrace(); }
                JSONArray roster = (JSONArray) jsonObject1.get("roster");

                String idSequence = "";
                idSize = 0;

                for(int i = 0; i < roster.size(); i++) {
                    JSONObject roster_item = (JSONObject) roster.get(i);
                    JSONObject person = (JSONObject) roster_item.get("person");
                    JSONObject pos = (JSONObject) roster_item.get("position");
                    String abbreviation = (String) pos.get("abbreviation");

                    if (!position.equals("P")) {
                        if (abbreviation.equals("P")) continue;
                        if (!position.equals("DH")) {
                            if (position.equals("LF") || position.equals("CF") || position.equals("RF")) {
                                if (!abbreviation.equals("OF") && !position.equals(abbreviation)) continue;
                            } else if (!position.equals(abbreviation)) continue;
                        }
                    }
                    else if (position.equals("P") && !abbreviation.equals("P") && !abbreviation.equals("TWP")) continue;
                    idSequence += Integer.parseInt(String.valueOf(person.get("id"))) + ",";
                    idSize++;
                }

                OkHttpClient client2 = new OkHttpClient();

                HttpUrl.Builder urlBuilder2 = HttpUrl.parse("https://statsapi.mlb.com/api/v1/people").newBuilder();
                urlBuilder2.addQueryParameter("personIds", idSequence);
                urlBuilder2.addQueryParameter("hydrate", "stats(type=season,season=" + year + ")");

                String url2 = urlBuilder2.build().toString();

                Request req2 = new Request.Builder().url(url2).build();

                client2.newCall(req2).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call c, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call c, @NonNull Response res) throws IOException {
                        final String myResponse2 = res.body().string();

                        JSONParser parser2 = new JSONParser();
                        JSONObject jsonObject2 = null;
                        try { jsonObject2 = (JSONObject) parser2.parse(myResponse2); }
                        catch (ParseException e) { e.printStackTrace(); }
                        JSONArray people = (JSONArray) jsonObject2.get("people");

                        for (int i = 0; i < idSize; i++) {
                            PlayerData playerData = new PlayerData();
                            try {
                                JSONObject people_item = (JSONObject) people.get(i);
                                JSONArray stats = (JSONArray) people_item.get("stats");
                                JSONObject stats_item = (JSONObject) stats.get(0);
                                JSONArray splits = (JSONArray) stats_item.get("splits");
                                JSONObject splits_item = (JSONObject) splits.get(0);
                                JSONObject stat = (JSONObject) splits_item.get("stat");

                                playerData.setId(Integer.parseInt(String.valueOf(people_item.get("id"))));
                                playerData.setName((String) people_item.get("fullName"));
                                if (!position.equals("P")) {
                                    playerData.setAtBats(Integer.parseInt(String.valueOf(stat.get("atBats"))));
                                    playerData.setHits(Integer.parseInt(String.valueOf(stat.get("hits"))));
                                    playerData.setHomeRuns(Integer.parseInt(String.valueOf(stat.get("homeRuns"))));
                                    playerData.setAvg((String) stat.get("avg"));
                                    playerData.setOps((String) stat.get("ops"));
                                }
                                else {
                                    playerData.setWins(Integer.parseInt(String.valueOf(stat.get("wins"))));
                                    playerData.setLosses(Integer.parseInt(String.valueOf(stat.get("losses"))));
                                    playerData.setEra((String) stat.get("era"));
                                    playerData.setHolds(Integer.parseInt(String.valueOf(stat.get("holds"))));
                                    playerData.setSaves(Integer.parseInt(String.valueOf(stat.get("saves"))));
                                }
                                playerList.add(playerData);
                            } catch (NullPointerException e) {
                                e.printStackTrace(); // api data omission
                            }
                        }
                        Log.d("num", String.valueOf(playerList.size()));
                        listener.onChanged(playerList);
                    }
                });
            }
        });
    }
}