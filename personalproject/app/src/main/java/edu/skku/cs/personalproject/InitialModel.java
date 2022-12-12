package edu.skku.cs.personalproject;

public class InitialModel implements Contract.ContractForInitialModel {
    private boolean mode;

    @Override
    public boolean getValue() {
        return mode;
    }

    @Override
    public void setValue(boolean mode, OnValueChangedListener listener) {
        this.mode = mode;
        listener.onChanged();
    }
}
