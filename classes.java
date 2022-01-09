import discord4j.core.object.entity.channel.Channel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;

public class classes extends ListenerAdapter
{
    JSONObject obj = new JSONObject();
    JSONArray array = new JSONArray();
    JSONParser parser = new JSONParser();
    public String newAuction(String name, String startingBid, String timeMinutes) {
        String FOS = "No info";
        try (FileReader reader = new FileReader(name+".json"))
        {
            JSONObject obj = (JSONObject) parser.parse(reader);
            String fileValue = (String) obj.get("startingBid");
            FOS = "Account already exists";
        }
        catch (FileNotFoundException e)
        {
            try (FileWriter userAccount = new FileWriter(name+".json")) {

                obj.put("Name:", name);
                obj.put("Current Bid:", Integer.valueOf(startingBid));
                obj.put("Duration:", Integer.valueOf(timeMinutes));
                obj.put("Bids:", 0);
                obj.put("Top Bidder:", "N/A");
                userAccount.write(obj.toJSONString());

            } catch (IOException a) {
                a.printStackTrace();
            } catch (ClassCastException a)
            {
                FOS = "Improper Typing";
            }
            FOS = "Success";
        }catch (IOException e)
        {
            FOS = "Failed";
            e.printStackTrace();
        } catch (ParseException e)
        {
            e.printStackTrace();
            FOS = "Faile";
        }
        return FOS;
    }

    public String[] getInfo(String name)
    {
        String[] fileValue = new String[4];
        try (FileReader reader = new FileReader(name+".json"))
        {
            JSONObject obj = (JSONObject) parser.parse(reader);
            fileValue[0] = String.valueOf(obj.get("Current Bid:"));
            fileValue[1] = String.valueOf(obj.get("Duration:"));
            fileValue[2] = String.valueOf(obj.get("Bids:"));
            fileValue[3] = String.valueOf(obj.get("Top Bidder:"));
        } catch (FileNotFoundException e) {
            fileValue[0] = "File couldnt be found";

        } catch (ParseException e) {
            e.printStackTrace();
            fileValue[0] = "Unspecified Error";
        }
        catch (ClassCastException e)
        {
            fileValue[0] = "Old or Corrupted account";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileValue;
    }
    public int incrementTime(String name, int duration)
    {

        String[] info = getInfo(name);
        int dur = Integer.parseInt(info[1])-1;
        try (FileReader reader = new FileReader(name+".json"))
        {
            JSONObject obj = (JSONObject) parser.parse(reader);
            try (FileWriter userAccount = new FileWriter(name+".json")) {

                obj.put("Name:", name);
                obj.put("Current Bid:", info[0]);
                obj.put("Duration:", dur);
                obj.put("Bids:", info[2]);
                obj.put("Top Bidder:", info[3]);
                userAccount.write(obj.toJSONString());

            } catch (IOException a) {
                a.printStackTrace();
            } catch (ClassCastException a)
            {
                return -1;
            }
        }
        catch (FileNotFoundException e)
        {
            return -4;
        }catch (IOException e)
        {
            e.printStackTrace();
            return -2;
        } catch (ParseException e)
        {
            e.printStackTrace();
            return -3;
        }
        return dur;


    }
    public int incrementBid(String name, int newBid, String bidder)
    {

        String[] info = getInfo(name);
        int bid = Integer.parseInt(info[0]);
        try (FileReader reader = new FileReader(name+".json"))
        {
            JSONObject obj = (JSONObject) parser.parse(reader);
            try (FileWriter userAccount = new FileWriter(name+".json")) {

                obj.put("Name:", name);
                obj.put("Current Bid:", newBid);
                obj.put("Duration:", info[1]);
                obj.put("Bids:", Integer.parseInt(info[2])+1);
                obj.put("Top Bidder:", bidder);
                userAccount.write(obj.toJSONString());

            } catch (IOException a) {
                a.printStackTrace();
            } catch (ClassCastException a)
            {
                return -1;
            }
        }
        catch (FileNotFoundException e)
        {

        }catch (IOException e)
        {
            e.printStackTrace();
            return -2;
        } catch (ParseException e)
        {
            e.printStackTrace();
            return -3;
        }
        return newBid;


    }
}
