package edu.skku.cs.personalproject;

public class InitialPresenter implements Contract.ContractForInitialPresenter, Contract.ContractForInitialModel.OnValueChangedListener {
    private Contract.ContractForInitialView view;
    private Contract.ContractForInitialModel model;

    public InitialPresenter(Contract.ContractForInitialView view, Contract.ContractForInitialModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onHitterButtonTouched() {
        model.setValue(false, this);
    }

    @Override
    public void onPitcherButtonTouched() {
        model.setValue(true, this);
    }

    @Override
    public void onChanged() {
        if(view != null) view.displayView(model.getValue());
    }
}
