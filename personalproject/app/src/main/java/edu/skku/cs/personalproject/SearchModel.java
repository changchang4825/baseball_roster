package edu.skku.cs.personalproject;

public class SearchModel implements Contract.ContractForSearchModel {
    private String[] teams = {"Los Angeles Angels", "Arizona Diamondbacks", "Baltimore Orioles", "Boston Red Sox", "Chicago Cubs", "Cincinnati Reds", "Cleveland Guardians",
            "Colorado Rockies", "Detroit Tigers", "Houston Astros", "Kansas City Royals", "Los Angeles Dodgers", "Washington Nationals", "New York Mets", "Oakland Athletics",
            "Pittsburgh Pirates", "San Diego Padres", "Seattle Mariners", "San Francisco Giants", "St. Louis Cardinals", "Tampa Bay Rays", "Texas Rangers", "Toronto Blue Jays",
            "Minnesota Twins", "Philadelphia Phillies", "Atlanta Braves", "Chicago White Sox", "Miami Marlins", "New York Yankees", "Milwaukee Brewers"};
    private String[] team_codes = {"108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "133", "134", "135", "136", "137", "138",
            "139", "140", "141", "142", "143", "144", "145", "146", "147", "158"};
    private String[] years = {"2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006",
            "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998"};
    private int team_idx, year_idx;
    private String position;

    public SearchModel(int team_idx, int year_idx, String position) {
        this.team_idx = team_idx;
        this.year_idx = year_idx;
        if (position.equals("CD")) this.position = "DH";
        else if (position.equals("P1") || position.equals("P2") || position.equals("P3") || position.equals("P4") || position.equals("P5")) this.position = "P";
        else this.position = position;
    }

    @Override
    public String[] getTeams() {
        return teams;
    }

    @Override
    public String[] getYears() {
        return years;
    }

    @Override
    public String[] getValues() {
        String[] value = {team_codes[team_idx], years[year_idx], position};
        return value;
    }

    @Override
    public boolean isValid() {
        return team_idx != -1 && year_idx != -1;
    }

    @Override
    public void setIndex(boolean mode, int idx) {
        if (mode == false) team_idx = idx;
        else year_idx = idx;
    }
}
