/** Copyright DoubleCloud Inc. */
package doublecloud.vim.rest.api.evt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import doublecloud.vim.rest.api.*;
import java.io.IOException;
import java.net.URLEncoder;

/**
 *
 * @author Steve
 */
public class ListEvents
{
  public static void main(String[] args) throws IOException {
    RestClient client = new RestClient("http://localhost:8080/api", "admin", "doublecloud");

    String ip = "192.168.0.200";
    String username = "root";
    String password = "doublecloud";
    client.addServer(ip, username, password);

    // for events per type, entity, time range - no need to have all but some
    String evtCol = "{'filter':{'category':['info'],"
            + "'eventTypeId':['VmPoweredOffEvent'],"
            + "'time':{'beginTime':'2017-12-07T09:18:10.001Z','endTime':'2017-12-08T09:18:10.001Z'},"
            + "'entity':{'entity':{'type':'VirtualMachine','val':'vm-3094'},'recursion':'self'}}}".replaceAll("'", "\"");
    String res = client.post("EventManager/" + ip + "/createCollectorForEvents", evtCol);
    System.out.println("res:" + res);

    Gson g = new Gson();
    JsonObject jo = g.fromJson(res, JsonObject.class);
    String id = jo.getAsJsonObject("returnval").get("val").getAsString();
    System.out.println(id);

    String relPath = "EventHistoryCollector/" + ip + ":" + URLEncoder.encode(id, "UTF-8");
    System.out.println(relPath);
    String collector = client.get(relPath);
    System.out.println(collector);

    String delPath = "EventHistoryCollector/" + ip + ":" + URLEncoder.encode(id, "UTF-8") + "/destroyCollector";
    System.out.println(delPath);
    String delRes = client.post(delPath, "");
    System.out.println(delRes);
  }
}
