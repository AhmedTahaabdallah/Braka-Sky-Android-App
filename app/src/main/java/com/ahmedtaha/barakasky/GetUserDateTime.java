package com.ahmedtaha.barakasky;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GetUserDateTime {
    private Context context;
    private DatabaseReference useresRef;
    private FirebaseAuth mAuth;
    //String current_user_country_Livesr;
    String currentUserId;
    String ss = " 5";
    public GetUserDateTime(Context current) {
        this.context = current;
    }

    /*private void getCurrentUserCountryLives(){
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        useresRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    current_user_country_Livesr = dataSnapshot.child("country_lives").getValue().toString();
                    ss = " 10";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    /*public void setMyAppDisplayLanguage() {
        String ll = Resources.getSystem().getConfiguration().locale.getLanguage();
        if (ll.equals("ar")){
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);
        } else {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);

        }

    }*/
    public String getDateToShow(String date_time, String current_user_country_Lives, String pattern_before, String pattern_after) {
        //getCurrentUserCountryLives();
        String la = Locale.getDefault().getDisplayLanguage();
        String da = "";
        if (la.equals("العربية")){
            try {
                Locale locale = new Locale("ar");
                String[] some_array = context.getResources().getStringArray(R.array.countries_lives_array);
                String country_live = "";
                if (current_user_country_Lives.equals("none")) {
                    country_live = "none";
                } else {
                    country_live = some_array[Integer.parseInt(current_user_country_Lives)];
                }
                String date_valu = getUserDate_Time(date_time, country_live, pattern_before);
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern_before);
                Date next_date = dateFormat.parse(date_valu);
                SimpleDateFormat fda = new SimpleDateFormat(pattern_after, locale);
                da = fda.format(next_date);
                //textView.setText(da);
            } catch (Exception e) {

            }
        } else{
            try {
                String[] some_array = context.getResources().getStringArray(R.array.countries_lives_array);
                String country_live = "";
                if (current_user_country_Lives.equals("none")) {
                    country_live = "none";
                } else {
                    country_live = some_array[Integer.parseInt(current_user_country_Lives)];
                }
                //Toast.makeText(MainActivity.this, country_live, Toast.LENGTH_LONG).show();
                String date_valu = getUserDate_Time(date_time, country_live, pattern_before);
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern_before);
                Date next_date = dateFormat.parse(date_valu);
                SimpleDateFormat fda = new SimpleDateFormat(pattern_after, Locale.ENGLISH);
                da = fda.format(next_date);
                //textView.setText(da);
            } catch (Exception e) {

            }
        }
        return  da;
    }
    public String getUserDate_Time(String dat, String country_lives, String date_pattern){
        String da = "";
        try {
            int my_hours = 0, my_minuits = 0;
            if (country_lives.equals("Egypt")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Afghanistan")){
                my_hours = 4;
                my_minuits = 30;
            } else if (country_lives.equals("Aland Islands")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Albania")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Algeria")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("American Samoa")){
                my_hours = -11;
                my_minuits = 0;
            } else if (country_lives.equals("Andorra")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Angola")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Anguilla")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Antarctica")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Antigua and Barbuda")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Argentina")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Armenia")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Aruba")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Western Australia")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("South Australia")){
                my_hours = 9;
                my_minuits = 30;
            } else if (country_lives.equals("Northern Australia")){
                my_hours = 9;
                my_minuits = 30;
            } else if (country_lives.equals("Australia New South Wales")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Australia Tasmania")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Australia Victoria")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Australian Capital Territory")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Australia Canberra capital city")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Australia Queensland")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Australia Lord Howe Island")){
                my_hours = 10;
                my_minuits = 30;
            } else if (country_lives.equals("Australia Macquarie Island")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Austria")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Azerbaijan")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Bahamas")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Bahrain")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Bangladesh")){
                my_hours = 6;
                my_minuits = 0;
            } else if (country_lives.equals("Barbados")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Belarus")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Belgium")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Belize")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("Benin")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Bermuda")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Bhutan")){
                my_hours = 6;
                my_minuits = 0;
            } else if (country_lives.equals("Bolivia")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Bonaire - Netherlands")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Bosnia and Herzegovina")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Botswana")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Bouvet Island")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Acre")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Southwest Amazonas")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Most of Amazonas")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Rondonia")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Roraima")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Alagoas")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Amapa")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Maranhao")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Para")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Piaui")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Ceara")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Sergipe")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Paraiba")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Pernambuco")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Rio Grande do Norte")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Bahia")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Tocantins")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Distrito Federal")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Espirito Santo")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Goias")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Minas Gerais")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Parana")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Rio de Janeiro")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Rio Grande do Sul")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Santa Catarina")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Sao Paulo")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Fernando de Noronha")){
                my_hours = -2;
                my_minuits = 0;
            } else if (country_lives.equals("Brazil Trindade")){
                my_hours = -2;
                my_minuits = 0;
            } else if (country_lives.equals("British Indian Ocean Territory")){
                my_hours = 6;
                my_minuits = 0;
            } else if (country_lives.equals("British Virgin Islands")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Brunei")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Bulgaria")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Burkina Faso")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Burundi")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Cabo Verde")){
                my_hours = -1;
                my_minuits = 0;
            } else if (country_lives.equals("Cambodia")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Cameroon")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Canada Toronto")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Canada Edmonton")){
                my_hours = -7;
                my_minuits = 0;
            } else if (country_lives.equals("Canada Winnipeg")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("Canada Vancouver")){
                my_hours = -8;
                my_minuits = 0;
            } else if (country_lives.equals("Canada Halifax")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Canada Saint Johns")){
                my_hours = -3;
                my_minuits = -30;
            } else if (country_lives.equals("Caribbean Netherlands")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Cape Verde")){
                my_hours = -1;
                my_minuits = 0;
            } else if (country_lives.equals("Cayman Islands")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Central African Republic")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Chad")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Chatham Islands")){
                my_hours = 12;
                my_minuits = 45;
            } else if (country_lives.equals("Chile main territory")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Chile Magallanes Region")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Chile Easter Island")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("China")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Christmas Island")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Cocos Islands")){
                my_hours = 6;
                my_minuits = 30;
            } else if (country_lives.equals("Colombia")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Comoros")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Congo Democratic Republic Western")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Congo Democratic Republic Eastern")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Cook Islands")){
                my_hours = -10;
                my_minuits = 0;
            } else if (country_lives.equals("Costa Rica")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("Cote dIvoire")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Croatia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Cuba")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Cyprus")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Czech Republic")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Democratic Republic of the Congo")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Denmark")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Djibouti")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Dominican Republic")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("East Timor")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Ecuador main territory")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Ecuador Galapagos Province")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("El Salvador")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("Equatorial Guinea")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Eritrea")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Estonia")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Eswatini")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Ethiopia")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Faeroe Islands")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Falkland Islands")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Fiji")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Finland")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Former Yugoslav Republic of Macedonia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("France")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("French Guiana")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("French Polynesia Gambier Islands")){
                my_hours = -9;
                my_minuits = 0;
            } else if (country_lives.equals("French Polynesia  Marquesas Islands")){
                my_hours = -9;
                my_minuits = 30;
            } else if (country_lives.equals("French Polynesia  Tahiti Island")){
                my_hours = -10;
                my_minuits = 0;
            } else if (country_lives.equals("French Southern Territories")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Gabon")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Gambia")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Georgia")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Germany")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Ghana")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Gibraltar")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Greece")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Denmark")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Denmark Thule Air Base")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Denmark Ittoqqortoormiit")){
                my_hours = -1;
                my_minuits = 0;
            } else if (country_lives.equals("Denmark Danmarkshavn")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Denmark Station Nord")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Grenada")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Guadeloupe")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Guam")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Guatemala")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("Guernsey")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Guinea")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Guinea-Bissau")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Guyana")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Haiti")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Heard Island and McDonald Islands")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Honduras")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("Hong Kong")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Hungary")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Iceland")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("India")){
                my_hours = 5;
                my_minuits = 30;
            } else if (country_lives.equals("Indonesia Sumatra Island")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia Java Island")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia West Kalimantan")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia Central Kalimantan")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia Sulawesi Island")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia Lesser Sunda Islands")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia North Kalimantan")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia East Kalimantan")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia South Kalimantan")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia Maluku Islands")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia Papua")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Indonesia West Papua")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Iran")){
                my_hours = 3;
                my_minuits = 30;
            } else if (country_lives.equals("Iraq")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Ireland")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Isle of Man")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Italy")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Jamaica")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Japan")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Jersey")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Jordan")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Kazakhstan Western")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Kazakhstan Eastern")){
                my_hours = 6;
                my_minuits = 0;
            } else if (country_lives.equals("Kenya")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Kerguelen Islands")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Kiribati Gilbert Islands")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Kiribati Phoenix Islands")){
                my_hours = 13;
                my_minuits = 0;
            } else if (country_lives.equals("Kiribati Line Islands")){
                my_hours = 14;
                my_minuits = 0;
            } else if (country_lives.equals("Kosovo")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Kuwait")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Kyrgyzstan")){
                my_hours = 6;
                my_minuits = 0;
            } else if (country_lives.equals("Laos")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Latvia")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Lebanon")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Lesotho")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Liberia")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Libya")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Liechtenstein")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Lithuania")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Luxembourg")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Macau")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Macedonia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Madagascar")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Malawi")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Malaysia")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Maldives")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Mali")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Malta")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Marshall Islands")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Martinique")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Mauritania")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Mauritius")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Mayotte")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico")){
                my_hours = 6;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico state of Quintana Roo")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico state of Baja California Sur")){
                my_hours = -7;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico Chihuahua")){
                my_hours = -7;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico Nayarit")){
                my_hours = -7;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico Sinaloa")){
                my_hours = -7;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico state of Sonora")){
                my_hours = -7;
                my_minuits = 0;
            } else if (country_lives.equals("Mexico state of Baja California")){
                my_hours = -8;
                my_minuits = 0;
            } else if (country_lives.equals("Micronesia  states of Chuuk and Yap")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Micronesia  states of Kosrae and Pohnpei")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Moldova")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Monaco")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Mongolia")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Mongolia provinces of Khovd")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Mongolia Uvs")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Mongolia Bayan-Olgii")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Montenegro")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Montserrat")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Morocco")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Mozambique")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Myanmar")){
                my_hours = 6;
                my_minuits = 30;
            } else if (country_lives.equals("Namibia")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Nauru")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Nepal")){
                my_hours = 5;
                my_minuits = 45;
            } else if (country_lives.equals("Netherlands")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Netherlands Antilles")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("New Caledonia")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("New Zealand")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Nicaragua")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("Niger")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Nigeria")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Niue")){
                my_hours = -11;
                my_minuits = 0;
            } else if (country_lives.equals("Norfolk Island")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("North Korea")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Northern Marianas")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Norway")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Oman")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Palestine")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Pakistan")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Palau")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Panama")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Papua New Guinea")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Papua New Guinea Bougainville Island")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Paraguay")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Peru")){
                my_hours = -5;
                my_minuits = 0;
            } else if (country_lives.equals("Philippines")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Pitcairn Islands")){
                my_hours = -8;
                my_minuits = 0;
            } else if (country_lives.equals("Poland")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Portugal")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Puerto Rico")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Qatar")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Reunion")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Romania")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Kamchatka")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Kaliningrad")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Moscow")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Samara")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Yekaterinburg")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Omsk")){
                my_hours = 6;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Kemerovo")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Irkutsk")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Yakutsk")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Vladivostok")){
                my_hours = 10;
                my_minuits = 0;
            } else if (country_lives.equals("Russia Srednekolymsk")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Rwanda")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Sao Tome and Principe")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Saint Barthelemy")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Saint Helena")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Saint Kitts and Nevis")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Saint Lucia")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Saint Martin")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Saint Pierre and Miquelon")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Saint Vincent and the Grenadines")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Samoa")){
                my_hours = 13;
                my_minuits = 0;
            } else if (country_lives.equals("San Marino")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Saudi Arabia")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Senegal")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Serbia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Seychelles")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("Sierra Leone")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Singapore")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Sint Eustatius")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Sint Maarten")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Slovakia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Slovenia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Solomon Islands")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Somalia")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("South Africa")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("South Georgia and the South Sandwich Islands")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("South Korea")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("South Sudan")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Spain")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Sri Lanka")){
                my_hours = 5;
                my_minuits = 30;
            } else if (country_lives.equals("Sudan")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Suriname")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Svalbard and Jan Mayen")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Swaziland")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Sweden")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Switzerland")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Syria")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("Taiwan")){
                my_hours = 8;
                my_minuits = 0;
            } else if (country_lives.equals("Tajikistan")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Tanzania")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Thailand")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Timor Leste")){
                my_hours = 9;
                my_minuits = 0;
            } else if (country_lives.equals("The Bahamas")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("The Gambia")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Togo")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("Tokelau")){
                my_hours = 13;
                my_minuits = 0;
            } else if (country_lives.equals("Tonga")){
                my_hours = 13;
                my_minuits = 0;
            } else if (country_lives.equals("Trinidad and Tobago")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Tunisia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Turkey")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Turkmenistan")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Turks and Caicos Islands")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Tuvalu")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Uganda")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Ukraine")){
                my_hours = 2;
                my_minuits = 0;
            } else if (country_lives.equals("United Arab Emirates")){
                my_hours = 4;
                my_minuits = 0;
            } else if (country_lives.equals("United Kingdom")){
                my_hours = 0;
                my_minuits = 0;
            } else if (country_lives.equals("United States Chicago")){
                my_hours = -6;
                my_minuits = 0;
            } else if (country_lives.equals("United States Los Angeles")){
                my_hours = -8;
                my_minuits = 0;
            } else if (country_lives.equals("United States Alaska")){
                my_hours = -9;
                my_minuits = 0;
            } else if (country_lives.equals("United States Hawaii")){
                my_hours = -10;
                my_minuits = 0;
            } else if (country_lives.equals("Uruguay")){
                my_hours = -3;
                my_minuits = 0;
            } else if (country_lives.equals("Uzbekistan")){
                my_hours = 5;
                my_minuits = 0;
            } else if (country_lives.equals("Vanuatu")){
                my_hours = 11;
                my_minuits = 0;
            } else if (country_lives.equals("Vatican City")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Venezuela")){
                my_hours = -4;
                my_minuits = 0;
            } else if (country_lives.equals("Vietnam")){
                my_hours = 7;
                my_minuits = 0;
            } else if (country_lives.equals("Wallis and Futuna")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Wake Island")){
                my_hours = 12;
                my_minuits = 0;
            } else if (country_lives.equals("Western Sahara")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Yemen")){
                my_hours = 3;
                my_minuits = 0;
            } else if (country_lives.equals("Yugoslavia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Zambia")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("Zimbabwe")){
                my_hours = 1;
                my_minuits = 0;
            } else if (country_lives.equals("none")){
                my_hours = 0;
                my_minuits = 0;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(date_pattern);
            Date user_date = dateFormat.parse(dat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user_date);
            calendar.add(Calendar.HOUR, my_hours);
            calendar.add(Calendar.MINUTE, my_minuits);
            SimpleDateFormat fda = new SimpleDateFormat(date_pattern);
            da = fda.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return da;
    }
}
