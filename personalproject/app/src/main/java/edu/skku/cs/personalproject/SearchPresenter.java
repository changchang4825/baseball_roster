package edu.skku.cs.personalproject;

public class SearchPresenter implements Contract.ContractForSearchPresenter {
    private Contract.ContractForSearchView view;
    private Contract.ContractForSearchModel model;

    public SearchPresenter(Contract.ContractForSearchView view, Contract.ContractForSearchModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public String[] requestTeams() {
        return model.getTeams();
    }

    @Override
    public String[] requestYears() {
        return model.getYears();
    }

    @Override
    public void onSpinnerSelected(boolean mode, int idx) {
        model.setIndex(mode, idx);
    }

    @Override
    public void onSearchButtonTouched() {
        if (model.isValid()) view.generateIntent(model.getValues());
        else view.displayWarningMessage();

    }
}
