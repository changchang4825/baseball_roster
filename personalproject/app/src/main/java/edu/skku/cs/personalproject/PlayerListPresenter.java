package edu.skku.cs.personalproject;

import java.util.ArrayList;

public class PlayerListPresenter implements Contract.ContractForPlayerListPresenter, Contract.ContractForPlayerListModel.OnValueChangedListener {
    private Contract.ContractForPlayerListView view;
    private Contract.ContractForPlayerListModel model;

    public PlayerListPresenter(Contract.ContractForPlayerListView view, Contract.ContractForPlayerListModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onWindowLoaded() {
        model.sendRequest(this);
    }

    @Override
    public void onChanged(ArrayList<PlayerData> playerList) {
        view.displayListView(model.getIsHitter(), playerList);
    }
}
