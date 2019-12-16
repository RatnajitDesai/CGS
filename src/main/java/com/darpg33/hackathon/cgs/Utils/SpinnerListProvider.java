package com.darpg33.hackathon.cgs.Utils;

import java.util.ArrayList;

public class SpinnerListProvider {


    public static ArrayList<String> getCountryList()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("India");
        list.add("Select Country");
        return list;

    }


    public static ArrayList<String> getGrievanceCategoriesList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("Road/Transport");
        list.add("Health/Hygiene");
        list.add("Water");
        list.add("Fraud");
        list.add("Bribery/Corruption");
        list.add("Administration");
        list.add("Select Grievance Category");

        return list;

    }

    public static ArrayList<String> getStates()
    {

        ArrayList<String> state = new ArrayList<>();

            state.add("Andaman and Nicobar Island");
            state.add("Andhra Pradesh");
            state.add("Arunachal Pradesh");
            state.add("Assam");
            state.add("Bihar");
            state.add("Chandigarh");
            state.add("Chhattisgarh");
            state.add("Dadra and Nagar Haveli");
            state.add("Daman and Diu");
            state.add("Delhi");
            state.add("Goa");
            state.add("Gujarat");
            state.add("Haryana");
            state.add("Himachal Pradesh");
            state.add("Jammu and Kashmir");
            state.add("Jharkhand");
            state.add("Karnataka");
            state.add("Kerala");
            state.add("Ladakh");
            state.add("Lakshadweep");
            state.add("Madhya Pradesh");
            state.add("Maharashtra");
            state.add("Manipur");
            state.add("Meghalaya");
            state.add("Mizoram");
            state.add("Nagaland");
            state.add("Odisha");
            state.add("Puducherry");
            state.add("Punjab");
            state.add("Rajasthan");
            state.add("Sikkim");
            state.add("Tamil Nadu");
            state.add("Telangana");
            state.add("Tripura");
            state.add("Uttarakhand");
            state.add("Uttar Pradesh");
            state.add("West Bengal");
            state.add("Select State");
            return state;
    }

    public static ArrayList<String> getDistricts(String state)
    {

        ArrayList<String> district = new ArrayList<>();
        switch (state) {
            case "Andhra Pradesh": {
                district = new ArrayList<>();
                district.add("Anantapur");
                district.add("Chittor");
                district.add("East Godavari");
                district.add("Guntur");
                district.add("Krishna");
                district.add("Kurnool");
                district.add("Prakasam");
                district.add("Srikakulam");
                district.add("Sri Potti Sriramulu Nellore");
                district.add("Visakhapatnam");
                district.add("Vizianagaram");
                district.add("West Godavari");
                district.add("YSR District, Kadapa");
                district.add("Select District");
                return district;
            }
            case "Andaman and Nicobar Island": {
                district = new ArrayList<>();
                district.add("Nicobar");
                district.add("North and Middle Andaman");
                district.add("South Andaman");
                district.add("Select District");
                return district;

            }
            case "Arunachal Pradesh": {
                district = new ArrayList<>();
                district.add("Anjaw");
                district.add("Changlang");
                district.add("Dibang Valley");
                district.add("East Kameng");
                district.add("East Siang");
                district.add("Kamle");
                district.add("Kra Daadi");
                district.add("Kurung Kumey");
                district.add("Lepa Rada");
                district.add("Lohit");
                district.add("Longdling");
                district.add("Lower Dibang Valley");
                district.add("Lower Siang");
                district.add("Lower Subansiri");
                district.add("Namsai");
                district.add("Pakke Kessang");
                district.add("Papum Pare");
                district.add("Shi Yomi");
                district.add("Siang");
                district.add("Tawang");
                district.add("Tirap");
                district.add("Upper Siang");
                district.add("Upper Subansiri");
                district.add("West Kameng");
                district.add("West Siang");
                district.add("Select District");
                return district;

            }
            case "Assam": {
                district = new ArrayList<>();
                district.add("Baksa");
                district.add("Barpeta");
                district.add("Biswanath");
                district.add("Bongaigaon");
                district.add("Cachar");
                district.add("Charaideo");
                district.add("Chirang");
                district.add("Darrang");
                district.add("Dhemaji");
                district.add("Dhubri");
                district.add("Dibrugarh");
                district.add("Dima Hasao (North Cachar Hills)");
                district.add("Goalpara");
                district.add("Golaghat");
                district.add("Hailakandi");
                district.add("Hojai");
                district.add("Jorhat");
                district.add("Kamrup");
                district.add("Kamrup Metropolitan");
                district.add("Karbi Anglong");
                district.add("Karimaganj");
                district.add("Kokrajhar");
                district.add("Lakhimpur");
                district.add("Majuli");
                district.add("Morigaon");
                district.add("Nagaon");
                district.add("Nalbari");
                district.add("Sivasagar");
                district.add("Sonitpur");
                district.add("South Salamara Mankachar");
                district.add("Tinsukia");
                district.add("Udakguri");
                district.add("West Karbi Anglong");
                district.add("Select District");
                return district;

            }

            case "Bihar": {
                district = new ArrayList<>();
                district.add("Araria");
                district.add("Arwal");
                district.add("Aurangabad");
                district.add("Banka");
                district.add("Begusarai");
                district.add("Bhagalpur");
                district.add("Bhojpur");
                district.add("Buxar");
                district.add("Darbhanga");
                district.add("East Champaran Motihari");
                district.add("Gaya");
                district.add("Gopalganji");
                district.add("Jamui");
                district.add("Jehanabad");
                district.add("Kaimur (Bhabua)");
                district.add("Katihar");
                district.add("Khagaria");
                district.add("Kishanganj");
                district.add("Lakhisarai");
                district.add("Madhepura");
                district.add("Madhubani");
                district.add("Munger (Monghyr)");
                district.add("Muzaffarpur");
                district.add("Nalanda");
                district.add("Nawada");
                district.add("Patna");
                district.add("Purnia (Purnea)");
                district.add("Rohtas");
                district.add("Saharasa");
                district.add("Samastipur");
                district.add("Saran");
                district.add("Sheikhpura");
                district.add("Sheohar");
                district.add("Sitamarhi");
                district.add("Siwan");
                district.add("Supaul");
                district.add("Vaishali");
                district.add("West Champaran");

                district.add("Select District");
                return district;
            }

            case "Chandigarh": {
                district = new ArrayList<>();
                district.add("Chandigarh");
                district.add("Select District");
                return district;

            }

            case "Chhattisgarh": {
                district = new ArrayList<>();
                district.add("Balod");
                district.add("Baloda Bazar");
                district.add("Balrampur");
                district.add("Bastar");
                district.add("Bemetara");
                district.add("Bijapur");
                district.add("Bilaspur");
                district.add("Dantewada (South Bastar)");
                district.add("Dhamtari");
                district.add("Durg");
                district.add("Gariyaband");
                district.add("Janjigir-Champa");
                district.add("Jashpur");
                district.add("Kabirdham (Kawardha)");
                district.add("Kanker (North Bastar)");
                district.add("Kondagaon");
                district.add("Korba");
                district.add("Korea (Koriya)");
                district.add("Mahasamund");
                district.add("Mungeli");
                district.add("Narayanpur");
                district.add("Raigarh");
                district.add("Raipur");
                district.add("Rajnandgaon");
                district.add("Sukma");
                district.add("Surajpur");
                district.add("Surguja");
                district.add("Select District");
                return district;

            }

            case "Dadra and Nagar Haveli": {
                district = new ArrayList<>();
                district.add("Dadra and Nagara Haveli");
                district.add("Select District");
                return district;
            }

            case "Daman and Diu": {
                district = new ArrayList<>();
                district.add("Daman");
                district.add("Diu");
                district.add("Select District");
                return district;

            }

            case "Delhi": {
                district = new ArrayList<>();
                district.add("Central Delhi");
                district.add("East Delhi");
                district.add("New Delhi");
                district.add("North Delhi");
                district.add("North East Delhi");
                district.add("North West Delhi");
                district.add("Shahdara");
                district.add("South Delhi");
                district.add("South East Delhi");
                district.add("South West Delhi");
                district.add("West Delhi");
                district.add("Select District");
                return district;

            }

            case "Goa": {
                district = new ArrayList<>();
                district.add("North Goa");
                district.add("South Goa");
                district.add("Select District");
                return district;

            }

            case "Gujarat": {
                district = new ArrayList<>();
                district.add("Ahmedabad");
                district.add("Amreli");
                district.add("Anand");
                district.add("Aravalli");
                district.add("Banaskantha");
                district.add("Bharuch");
                district.add("Bhavnagar");
                district.add("Botad");
                district.add("Chhota Udaipur");
                district.add("Dahod");
                district.add("Dang");
                district.add("Devbhoomi Dwarka");
                district.add("Gandhinagar");
                district.add("Gir Somnath");
                district.add("Jamnagar");
                district.add("Junagadh");
                district.add("Kachchh");
                district.add("Kheda");
                district.add("Mahisagar");
                district.add("Mehsana");
                district.add("Morbi");
                district.add("Narmada");
                district.add("Navsari");
                district.add("Panchmahal");
                district.add("Patan");
                district.add("Porbandar");
                district.add("Rajkot");
                district.add("Sabarkantha");
                district.add("Surat");
                district.add("Surendranagar");
                district.add("Tapi");
                district.add("Vadodara");
                district.add("Valsad");
                district.add("Select District");
                return district;

            }

            case "Haryana": {
                district = new ArrayList<>();
                district.add("Ambala");
                district.add("Bhiwani");
                district.add("Charkhi Dadri");
                district.add("Faridabad");
                district.add("Fatehabad");
                district.add("Gurugram");
                district.add("Hisar");
                district.add("Jhajjar");
                district.add("Jind");
                district.add("Kaithal");
                district.add("Karnal");
                district.add("Kurukshetra");
                district.add("Mahendragarh");
                district.add("Nuh");
                district.add("Palwal");
                district.add("Panchkula");
                district.add("Panipat");
                district.add("Rewari");
                district.add("Rohtak");
                district.add("Sirsa");
                district.add("Sonipat");
                district.add("Yamunanagar");
                district.add("Select District");
                return district;

            }


            case "Himachal Pradesh": {
                district = new ArrayList<>();
                district.add("Bilaspur");
                district.add("Chamba");
                district.add("Hamirpur");
                district.add("Kangra");
                district.add("Kinnaur");
                district.add("Kullu");
                district.add("Lahaul & Spiti");
                district.add("Mandi");
                district.add("Shimla");
                district.add("Sirmaur");
                district.add("Solan");
                district.add("Una");
                district.add("Select District");
                return district;

            }


            case "Jammu and Kashmir": {
                district = new ArrayList<>();
                district.add("Anantnag");
                district.add("Bandipore");
                district.add("Baramulla");
                district.add("Budgam");
                district.add("Doda");
                district.add("Ganderbal");
                district.add("Jammu");
                district.add("Kathua");
                district.add("Kishtwar");
                district.add("Kulgam");
                district.add("Kupwara");
                district.add("Mirpur");
                district.add("Muzaffarabad");
                district.add("Poonch");
                district.add("Pulwama");
                district.add("Rajouri");
                district.add("Ramban");
                district.add("Reasi");
                district.add("Samba");
                district.add("Shopian");
                district.add("Srinagar");
                district.add("Udhampur");
                district.add("Select District");
                return district;

            }

            case "Jharkhand": {
                district = new ArrayList<>();
                district.add("Bokaro");
                district.add("Chatra");
                district.add("Deoghar");
                district.add("Dhanbad");
                district.add("Dumka");
                district.add("East Singhbhum");
                district.add("Garhwa");
                district.add("Giridih");
                district.add("Godda");
                district.add("Gumla");
                district.add("Hazaribag");
                district.add("Jamtara");
                district.add("Khunti");
                district.add("Koderma");
                district.add("Latehar");
                district.add("Lohardaga");
                district.add("Pakur");
                district.add("Palamu");
                district.add("Ramgarh");
                district.add("Ranchi");
                district.add("Sahibganj");
                district.add("Seraikela-Kharsawan");
                district.add("Simdega");
                district.add("West Singhbhum");
                district.add("Select District");
                return district;
            }

            case "Karnataka": {
                district = new ArrayList<>();
                district.add("Bagalkot");
                district.add("Ballari (Bellary)");
                district.add("Belagavi (Begaum)");
                district.add("Bengaluru (Bangalore) Rural");
                district.add("Bengaluru (Bangalore) Urban");
                district.add("Bidar");
                district.add("Chamarajanagar");
                district.add("Chikballapur");
                district.add("Chikkamagaluru (Chikmagalur)");
                district.add("Chitradurga");
                district.add("Dakshina Kannada");
                district.add("Davangere");
                district.add("Dharwad");
                district.add("Gadag");
                district.add("Hassan");
                district.add("Haveri");
                district.add("Kalaburagi (Gulbaraga)");
                district.add("Kodagu");
                district.add("Kolar");
                district.add("Koppal");
                district.add("Mandya");
                district.add("Mysuru (Mysore)");
                district.add("Raichur");
                district.add("Ramanagara");
                district.add("Shivamogga (Shimoga)");
                district.add("Tumakuru (Tumkur)");
                district.add("Udupi");
                district.add("Uttara Kannada (Karwar)");
                district.add("Vijayapura (Bijapur)");
                district.add("Yadgir");
                district.add("Select District");
                return district;

            }

            case "Kerala": {
                district = new ArrayList<>();
                district.add("Alappuzha");
                district.add("Ernakulam");
                district.add("Idukki");
                district.add("Kannur");
                district.add("Kasaragod");
                district.add("Kollam");
                district.add("Kottayam");
                district.add("Kozhikode");
                district.add("Malappuram");
                district.add("Palakkad");
                district.add("Pathanamthitta");
                district.add("Thiruvananthapuram");
                district.add("Thrissur");
                district.add("Wayanad");
                district.add("Select District");
                return district;

            }

            case "Ladakh": {
                district = new ArrayList<>();
                district.add("Kargil");
                district.add("Leh");
                district.add("Select District");
                return district;

            }

            case "Lakshadweep": {
                district = new ArrayList<>();
                district.add("Lakshadweep");
                district.add("Select District");
                return district;

            }

            case "Madhya Pradesh": {
                district = new ArrayList<>();
                district.add("Agar Malwa");
                district.add("Alirajpur");
                district.add("Anuppur");
                district.add("Ashoknagar");
                district.add("Balaghat");
                district.add("Barwani");
                district.add("Betul");
                district.add("Bhind");
                district.add("Bhopal");
                district.add("Burhanpur");
                district.add("Chhatarpur");
                district.add("Chhindwara");
                district.add("Damoh");
                district.add("Datia");
                district.add("Dewas");
                district.add("Dhar");
                district.add("Dindori");
                district.add("Guna");
                district.add("Gwalior");
                district.add("Harda");
                district.add("Hoshangabad");
                district.add("Indore");
                district.add("Jabalpur");
                district.add("Jhabua");
                district.add("Katni");
                district.add("Khandwa");
                district.add("Khargone");
                district.add("Mandla");
                district.add("Mandsaur");
                district.add("Morena");
                district.add("Narsinghpur");
                district.add("Neemuch");
                district.add("Panna");
                district.add("Raisen");
                district.add("Rajgarh");
                district.add("Ratlam");
                district.add("Rewa");
                district.add("Sagar");
                district.add("Satna");
                district.add("Sehore");
                district.add("Seoni");
                district.add("Shahdol");
                district.add("Shajapur");
                district.add("Sheopur");
                district.add("Shivpuri");
                district.add("Sidhi");
                district.add("Singrauli");
                district.add("Tikamgarh");
                district.add("Ujjain");
                district.add("Umaria");
                district.add("Vidisha");
                district.add("Select District");
                return district;

            }

            case "Maharashtra": {
                district = new ArrayList<>();
                district.add("Ahmednagar");
                district.add("Akola");
                district.add("Amravati");
                district.add("Aurangabad");
                district.add("Beed");
                district.add("Bhandara");
                district.add("Buldhana");
                district.add("Chandrapur");
                district.add("Dhule");
                district.add("Gadchiroli");
                district.add("Gondia");
                district.add("Hingoli");
                district.add("Jalgaon");
                district.add("Jalna");
                district.add("Kolhapur");
                district.add("Latur");
                district.add("Mumbai City");
                district.add("Mumbai Suburban");
                district.add("Nagpur");
                district.add("Nanded");
                district.add("Nandurbar");
                district.add("Nashik");
                district.add("Osmanabad");
                district.add("Palghar");
                district.add("Parbhani");
                district.add("Pune");
                district.add("Raigad");
                district.add("Ratnagiri");
                district.add("Sangli");
                district.add("Satara");
                district.add("Sindhudurg");
                district.add("Solapur");
                district.add("Thane");
                district.add("Wardha");
                district.add("Washim");
                district.add("Yavatmal");
                district.add("Select District");
                return district;

            }

            case "Manipur": {
                district = new ArrayList<>();
                district.add("Bishnupur");
                district.add("Chandel");
                district.add("Churachandpur");
                district.add("Imphal East");
                district.add("Imphal West");
                district.add("Jiribam");
                district.add("Kakching");
                district.add("Kamjong");
                district.add("Kangpokpi");
                district.add("Noney");
                district.add("Pherzawl");
                district.add("Senapati");
                district.add("Tamenglong");
                district.add("Tengnoupal");
                district.add("Thoubal");
                district.add("Ukhrul");
                district.add("Select District");
                return district;

            }


            case "Meghalaya": {
                district = new ArrayList<>();
                district.add("East Garo Hills");
                district.add("East Jaintia Hills");
                district.add("East Khasi Hills");
                district.add("North Garo Hills");
                district.add("Ri Bhoi");
                district.add("South Garo Hills");
                district.add("South West Garo Hills");
                district.add("South West Khasi Hills");
                district.add("West Garo Hills");
                district.add("West Jaintia Hills");
                district.add("West Khasi Hills");
                district.add("Select District");
                return district;

            }

            case "Mizoram": {
                district = new ArrayList<>();
                district.add("Aizawl");
                district.add("Champhai");
                district.add("Kolasib");
                district.add("Lawngtlai");
                district.add("Lunglei");
                district.add("Mamit");
                district.add("Saiha");
                district.add("Serchhip");
                district.add("Select District");
                return district;

            }

            case "Nagaland": {
                district = new ArrayList<>();
                district.add("Dimapur");
                district.add("Kiphire");
                district.add("Kohima");
                district.add("Longleng");
                district.add("Mokokchung");
                district.add("Mon");
                district.add("Peren");
                district.add("Phek");
                district.add("Tuensang");
                district.add("Wokha");
                district.add("Zunheboto");
                district.add("Select District");
                return district;

            }

            case "Odisha": {
                district = new ArrayList<>();
                district.add("Angul");
                district.add("Balangir");
                district.add("Balasore");
                district.add("Bargarh");
                district.add("Bhadrak");
                district.add("Boudh");
                district.add("Cuttack");
                district.add("Deogarh");
                district.add("Dhenkanal");
                district.add("Gajapati");
                district.add("Ganjam");
                district.add("Jagatsinghapur");
                district.add("Jajpur");
                district.add("Jharsuguda");
                district.add("Kalahandi");
                district.add("Kandhamal");
                district.add("Kendrapara");
                district.add("Kendujhar (Keonjhar)");
                district.add("Khordha");
                district.add("Koraput");
                district.add("Malkangiri");
                district.add("Mayurbhanj");
                district.add("Nabarangpur");
                district.add("Nayagarh");
                district.add("Nuapada");
                district.add("Puri");
                district.add("Rayagada");
                district.add("Sambalpur");
                district.add("Sonepur");
                district.add("Sundargarh");
                district.add("Select District");
                return district;

            }

            case "Puducherry": {
                district = new ArrayList<>();
                district.add("Karaikal");
                district.add("Mahe");
                district.add("Pondicherry");
                district.add("Yanam");
                district.add("Select District");
                return district;

            }

            case "Punjab": {
                district = new ArrayList<>();
                district.add("Amritsar");
                district.add("Barnala");
                district.add("Bathinda");
                district.add("Faridkot");
                district.add("Fatehgarh Sahib");
                district.add("Fazilka");
                district.add("Ferozepur");
                district.add("Gurdaspur");
                district.add("Hoshiarpur");
                district.add("Jalandhar");
                district.add("Kapurthala");
                district.add("Ludhiana");
                district.add("Mansa");
                district.add("Moga");
                district.add("Muktsar");
                district.add("Nawanshahr (Shahid Bhagat Singh Nagar)");
                district.add("Parhankot");
                district.add("Patiala");
                district.add("Rupnagar");
                district.add("Sahibzada Ajit Singh Nagar (Mohali)");
                district.add("Sangrur");
                district.add("Tarn Taran");
                district.add("Select District");
                return district;

            }

            case "Rajasthan": {
                district = new ArrayList<>();
                district.add("Ajmer");
                district.add("Alwar");
                district.add("Banswara");
                district.add("Baran");
                district.add("Barmer");
                district.add("Bharatpur");
                district.add("Bhilwara");
                district.add("Bikaner");
                district.add("Bundi");
                district.add("Chittorgarh");
                district.add("Churu");
                district.add("Dausa");
                district.add("Dholpur");
                district.add("Dungarpur");
                district.add("Hanumangarh");
                district.add("Jaipur");
                district.add("Jaisalmer");
                district.add("Jalore");
                district.add("Jhalawar");
                district.add("Jhunjhunu");
                district.add("Jodhpur");
                district.add("Karauli");
                district.add("Kota");
                district.add("Nagaur");
                district.add("Pali");
                district.add("Pratapgarh");
                district.add("Rajsamand");
                district.add("Sawai Madhopur");
                district.add("Sikar");
                district.add("Sirohi");
                district.add("Sri Ganganagar");
                district.add("Tonk");
                district.add("Udaipur");
                district.add("Select District");
                return district;

            }

            case "Sikkim": {
                district = new ArrayList<>();
                district.add("East Sikkim");
                district.add("North Sikkim");
                district.add("South Sikkim");
                district.add("West Sikkim");
                district.add("Select District");
                return district;

            }

            case "Tamil Nadu": {
                district = new ArrayList<>();
                district.add("Ariyalur");
                district.add("Chengalpet");
                district.add("Chennai");
                district.add("Coimbatore");
                district.add("Cuddalore");
                district.add("Dharmapuri");
                district.add("Dindigul");
                district.add("Erode");
                district.add("Kallakurichi");
                district.add("Kanchipuram");
                district.add("Kanyakumari");
                district.add("Karur");
                district.add("Krishnagiri");
                district.add("Madurai");
                district.add("Nagapattinam");
                district.add("Namakkal");
                district.add("Nilgiris");
                district.add("Perambalur");
                district.add("Pudkkottai");
                district.add("Ramanathapuram");
                district.add("Ranipet");
                district.add("Salem");
                district.add("Sivanganga");
                district.add("Tenkasi");
                district.add("Thanjavur");
                district.add("Theni");
                district.add("Thoothukundi (Tuticorin)");
                district.add("Tiruchirappalli");
                district.add("Tirunelveli");
                district.add("Tirupattur");
                district.add("Tiruppur");
                district.add("Tiruvallur");
                district.add("Tiruvannamalai");
                district.add("Tiruvarur");
                district.add("Vellore");
                district.add("Viluppuram");
                district.add("Virdhunagar");
                district.add("Select District");
                return district;

            }

            case "Telangana": {
                district = new ArrayList<>();
                district.add("Adilabad");
                district.add("Bhadradri Kothagudem");
                district.add("Hyderabad");
                district.add("Jagital");
                district.add("Jangaon");
                district.add("Jayashankar Bhoopalpally");
                district.add("Jogulamba Gadwal");
                district.add("Kamareddy");
                district.add("Karimnagar");
                district.add("Khammam");
                district.add("Komaram Bheem Asifabad");
                district.add("Mahabubabad");
                district.add("Mahabubnagar");
                district.add("Mancherial");
                district.add("Medak");
                district.add("Medchal");
                district.add("Mulugu");
                district.add("Nagarkurnool");
                district.add("Nalgonda");
                district.add("Narayanpet");
                district.add("Nirmal");
                district.add("Nizamabad");
                district.add("Peddapalli");
                district.add("Rajanna Sircilla");
                district.add("Rangareddy");
                district.add("Sangareddy");
                district.add("Siddipet");
                district.add("Suryapet");
                district.add("Vikarabad");
                district.add("Wanaparthy");
                district.add("Warangal (Rural)");
                district.add("Warangal (Urban)");
                district.add("Yadadri Bhuvanagiri");
                district.add("Select District");
                return district;

            }

            case "Tripura": {
                district = new ArrayList<>();
                district.add("Dhalai");
                district.add("Gomati");
                district.add("Khowai");
                district.add("North Tripura");
                district.add("Sepahijala");
                district.add("South Tripura");
                district.add("Unakoti");
                district.add("West Tripura");
                district.add("Select District");
                return district;

            }

            case "Uttarakhand": {
                district = new ArrayList<>();
                district.add("Almora");
                district.add("Bageshwar");
                district.add("Chamoli");
                district.add("Champawat");
                district.add("Dehradun");
                district.add("Haridwar");
                district.add("Nainital");
                district.add("Pauri Garhwal");
                district.add("Pithoragarh");
                district.add("Rudraprayag");
                district.add("Tehri Garhwal");
                district.add("Udham Singh Nagar");
                district.add("Uttarkashi");
                district.add("Select District");
                return district;

            }

            case "Uttar Pradesh": {
                district = new ArrayList<>();
                district.add("Agra");
                district.add("Aligarh");
                district.add("Allahabad");
                district.add("Ambedkar Nagar");
                district.add("Amethi (Chatrapati sahuji Mahraj Nagar)");
                district.add("Amroha (J.P.Nagar)");
                district.add("Auraiya");
                district.add("Azangarh");
                district.add("Baghpat");
                district.add("Bahraich");
                district.add("Ballia");
                district.add("Balrampur");
                district.add("Banda");
                district.add("Barabanki");
                district.add("Bareilly");
                district.add("Basti");
                district.add("Bhadohi");
                district.add("Bijnor");
                district.add("Budaun");
                district.add("Buldandshahr");
                district.add("Chandauli");
                district.add("Chitrakoot");
                district.add("Deoria");
                district.add("Etah");
                district.add("Etawah");
                district.add("Faizabad");
                district.add("Farrukhabad");
                district.add("Fatehpur");
                district.add("Firozabad");
                district.add("Gautam Buddha Nagar");
                district.add("Ghaziabad");
                district.add("Ghazipur");
                district.add("Gonda");
                district.add("Gorakhpur");
                district.add("Hamirpur");
                district.add("Hapur (Panchsheel Nagar)");
                district.add("Hardoi");
                district.add("Hathras");
                district.add("Jalaun");
                district.add("Jaunpur");
                district.add("Jhansi");
                district.add("Kannauj");
                district.add("Kanpur Dehat");
                district.add("Kanpur Nagar");
                district.add("Kanshiram Nagar (Kasganj)");
                district.add("Kaushambi");
                district.add("Kushinagar (Padrauna)");
                district.add("Lakhimpur - Kheri");
                district.add("Lalitpur");
                district.add("Lucknow");
                district.add("Maharajganj");
                district.add("Mahoba");
                district.add("Mainpuri");
                district.add("Mathura");
                district.add("Mau");
                district.add("Meerut");
                district.add("Mizapur");
                district.add("Moradabad");
                district.add("Muzaffarnagar");
                district.add("Pilibhit");
                district.add("Pratapgarh");
                district.add("RaeBareli");
                district.add("Rampur");
                district.add("Saharanpur");
                district.add("Sambhal (Bhim Nagar)");
                district.add("Sant Kabir Nagar");
                district.add("Shahjahanpur");
                district.add("Shamali (Prabuddh Nagar)");
                district.add("Shravasti");
                district.add("Siddharth Nagar");
                district.add("Sitapur");
                district.add("Sonbhandra");
                district.add("Sultanpur");
                district.add("Unnao");
                district.add("Varanasi");
                district.add("Select District");
                return district;

            }
            case "West Bengal": {
                district = new ArrayList<>();
                district.add("Alipurduar");
                district.add("Bankura");
                district.add("Birbhum");
                district.add("Cooch Behar");
                district.add("Dakshin Dinajpur (South Dinajpur)");
                district.add("Darjeeling");
                district.add("Hooghly");
                district.add("Howrah");
                district.add("Jalpaiguri");
                district.add("Jhargram");
                district.add("Kalimpong");
                district.add("Kolkata");
                district.add("Malda");
                district.add("Murshidabad");
                district.add("Nadia");
                district.add("North 24 Parganas");
                district.add("Paschim (West) Burdwan (Bardhaman)");
                district.add("Paschim Medinipur (West Medinipur)");
                district.add("Purba Burdwan (Bardhaman)");
                district.add("Purba Medinipur (East Medinipur)");
                district.add("Purulia");
                district.add("South 24 Parganas");
                district.add("Uttar Dinajpur (North Dinajpur)");
                district.add("Select District");
                return district;
            }
            case "Select State":
            {
                district = new ArrayList<>();
                district.add("Select District");
                district.add("Select District");
                return district;
            }

        }

        return district;

    }

}
