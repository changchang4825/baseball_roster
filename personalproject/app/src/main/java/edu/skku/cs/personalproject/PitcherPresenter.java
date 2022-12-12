package edu.skku.cs.personalproject;

import java.util.ArrayList;

public class PitcherPresenter implements Contract.ContractForHitterPresenter, Contract.ContractForHitterModel.OnValueChangedListener {
    private Contract.ContractForHitterView view;
    private Contract.ContractForHitterModel model;

    public PitcherPresenter(Contract.ContractForHitterView view, Contract.ContractForHitterModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onPlayerButtonTouched(String position) {
        model.setPosition(position, this);
    }

    @Override
    public void onCandidateButtonTouched(String position, int id) {
        model.setValue(position, id, this);
    }

    @Override
    public void onIntentResultReturned(int id) {
        String pos = model.getPosition();
        if (pos.equals("P")) {
            if (model.getDeletedId() != -1) model.changeId(id, this);
            else model.addId(id, this);
        }
        else model.updatePlayer(id, this);
    }

    @Override
    public void onResetButtonTouched() {
        model.resetIdList();
        view.resetView();
    }

    @Override
    public void onWindowLoaded() {
        model.loadData(this);
    }

    @Override
    public void onChanged() {
        view.generateIntent(model.getPosition());
    }

    @Override
    public void onCandidatesIdListChanged(int num, ArrayList<Integer> idList) {
        view.displayCandidate(num, idList);
    }

    @Override
    public void onPlayersIdListChanged(String position, int id) {
        view.displayPlayer(id, position);
    }

    @Override
    public void onFail() {
        view.displayFail();
    }
}