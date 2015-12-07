package barqsoft.footballscores;

import android.content.Context;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    //public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
   public static final int CHAMPIONS_LEAGUE = 362;
  // public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;


    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMERA_LIGA = 402;
    public static final int Bundesliga3 = 403;
    public static final int REDIVISIEE = 404;


    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            //case SERIE_A : return "Seria A";
            case PREMIER_LEGAUE : return "Premier League";
            case CHAMPIONS_LEAGUE : return "UEFA Champions League";
            //case PRIMERA_DIVISION : return "Primera Division";
            case BUNDESLIGA : return "Bundesliga";


            case BUNDESLIGA1 : return "1. Bundesliga 2015/16";
            case BUNDESLIGA2: return "2. Bundesliga 2015/16";
            case LIGUE1 : return "Ligue 1 2015/16";
            case LIGUE2: return "Ligue 2 2015/16";
            case PREMIER_LEAGUE : return "Premier League 2015/16";
            case PRIMERA_DIVISION : return "Primera Division 2015/16";
            case SEGUNDA_DIVISION : return"Segunda Division 2015/16";
            case SERIE_A : return "Serie A 2015/16";
            case PRIMERA_LIGA : return "Primeira Liga 2015/16";
            case Bundesliga3 : return "3. Bundesliga 2015/16";
            case REDIVISIEE : return "Eredivisie 2015/16";
            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num,Context context)
    {

        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {

                return context.getResources().getString(R.string.Group_Stages_Matchday6);
            }
            else if(match_day == 7 || match_day == 8)
            {
                return context.getResources().getString(R.string.First_Knockout_Round);

            }
            else if(match_day == 9 || match_day == 10)
            {
                return context.getResources().getString(R.string.QuarterFinal);

            }
            else if(match_day == 11 || match_day == 12)
            {
                return context.getResources().getString(R.string.SemiFinal);

            }
            else
            {
                return context.getResources().getString(R.string.Final);

            }
        }
        else
        {
            return context.getResources().getString(R.string.Matchday) + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Aston Villa FC" : return R.drawable.aston_villa;
            case "bureny_fc_hd" : return R.drawable.burney_fc_hd_logo;
            case "Chelsea FC" : return R.drawable.chelsea;
            case "Crestial Palace FC" : return R.drawable.crystal_palace_fc;

            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "Hull City AFC" : return R.drawable.hull_city_afc_hd_logo;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;

            case "Liverpool FC" : return R.drawable.liverpool;
            case "Manchester City FC" : return R.drawable.manchester_city;
            case "Manchester United FC" : return R.drawable.manchester_united;

            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Newcaslte United" : return R.drawable.newcastle_united;
            case "Queen Park Rangers" : return R.drawable.queens_park_rangers_hd_logo;
            case "South Hampton FC" : return R.drawable.southampton_fc;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "SwanSea City FC" : return R.drawable.swansea_city_afc;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Villarreal CF" : return R.drawable.villarreal;
            default: return R.drawable.no_icon;
        }
    }
}
