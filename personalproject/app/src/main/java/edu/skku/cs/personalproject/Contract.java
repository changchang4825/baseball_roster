package edu.skku.cs.personalproject;

import java.util.ArrayList;

public interface Contract {
    interface ContractForInitialView {
        void displayView(boolean mode);
    }
    interface ContractForInitialModel {
        boolean getValue();
        void setValue(boolean mode, OnValueChangedListener listener);
        interface OnValueChangedListener {
            void onChanged();
        }
    }
    interface ContractForInitialPresenter {
        void onHitterButtonTouched();
        void onPitcherButtonTouched();
    }

    interface ContractForHitterView {
        void generateIntent(String position);
        void displayPlayer(int id, String position);
        void displayCandidate(int num, ArrayList<Integer> candidatesIdList);
        void resetView();
        void displayFail();
    }
    interface ContractForHitterModel {
        String getPosition();
        int getDeletedId();
        void setPosition(String position, OnValueChangedListener listener);
        void setValue(String position, int id, OnValueChangedListener listener);
        void addId(int id, OnValueChangedListener listener);
        void changeId(int id, OnValueChangedListener listener);
        void resetIdList();
        void updatePlayer(int id, OnValueChangedListener listener);
        void loadData(OnValueChangedListener listener);
        interface OnValueChangedListener {
            void onChanged();
            void onCandidatesIdListChanged(int num, ArrayList<Integer> candidatesIdList);
            void onPlayersIdListChanged(String position, int id);
            void onFail();
        }
    }
    interface ContractForHitterPresenter {
        void onPlayerButtonTouched(String position);
        void onCandidateButtonTouched(String position, int id);
        void onIntentResultReturned(int id);
        void onResetButtonTouched();
        void onWindowLoaded();
    }

    interface ContractForSearchView {
        void generateIntent(String[] values);
        void displayWarningMessage();
    }
    interface ContractForSearchModel {
        String[] getTeams();
        String[] getYears();
        String[] getValues();
        boolean isValid();
        void setIndex(boolean mode, int position);
    }
    interface ContractForSearchPresenter {
        String[] requestTeams();
        String[] requestYears();
        void onSpinnerSelected(boolean mode, int idx);
        void onSearchButtonTouched();
    }

    interface ContractForPlayerListView {
        void displayListView(boolean isHitter, ArrayList<PlayerData> playerList);
    }
    interface ContractForPlayerListModel {
        boolean getIsHitter();
        void sendRequest(OnValueChangedListener listener);
        interface OnValueChangedListener {
            void onChanged(ArrayList<PlayerData> playerList);
        }
    }
    interface ContractForPlayerListPresenter {
        void onWindowLoaded();
    }
}
