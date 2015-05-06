package com.walkwithme.clusterserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@SuppressWarnings("serial")
public class ClusteringServerServlet extends HttpServlet {

    HashMap<Integer, List<Integer>> zoneToGroups = new HashMap<>();
    HashMap<Integer, Group> groups = new HashMap<>();
    HashMap<Integer, Group> tempGroups = new HashMap<>();
    Integer counter = 1;
    ZoneContainer zoneContainer = new ZoneContainer();
    Gson gson = new Gson();

    public int getNextCounterVal() {
        return counter++;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("Inside doGet");
        String response = null;
        String methodName = req.getParameter("method");
        System.out.println(methodName);
        if (methodName.equals("request1")) {
            System.out.println("request1");
            String latLng = req.getParameter("latLng");
            String time = req.getParameter("time");
            String user = req.getParameter("user");
            response = processRequest1(user, latLng, time);
        } else if (methodName.equals("request2")) {
            String latLng = req.getParameter("latLng");
            String groupId = req.getParameter("groupId");
            String time = req.getParameter("time");
            String user = req.getParameter("user");
            response = processRequest2(user, latLng, groupId, time);
        }
        resp.getWriter().println(response);
        resp.getWriter().flush();
    }

    private String processRequest2(String user, String latLng, String groupId,
            String time) {
        Integer groupIdInt = Integer.parseInt(groupId);
        Group group = groups.get(groupIdInt);
        if (group == null) {
            group = tempGroups.remove(groupIdInt);
            groups.put(Integer.parseInt(groupId), group);
            Integer zoneId = zoneContainer.findZoneId(latLng);
            List<Integer> groupIds = zoneToGroups.get(zoneId);
            if (groupIds == null) {
                groupIds = new ArrayList<>();
                zoneToGroups.put(zoneId, groupIds);
            }
            groupIds.add(groupIdInt);
        } else {
            group.latLongs.add(latLng);
        }
        return "success";
    }

    private String processRequest1(String user, String latLng, String time) {
        // Request1 addReq = gson.fromJson(json, Request1.class);
        String retVal = null;

        Integer zoneId = zoneContainer.findZoneId(latLng);
        if (zoneId == null) {
            return "no_zone";
        }
        List<Integer> groupIds = zoneToGroups.get(zoneId);
        Integer idealGroup = 0;
        List<Group> groupList = new ArrayList<>();
        if (groupIds != null && groupIds.size() != 0) {
            for (Integer groupId : groupIds) {
                Group group = groups.get(groupId);
                groupList.add(group);
                if (group.time.equals(time)) {
                    idealGroup = group.groupId;
                }
            }
            Integer createGroupId = 0;
            if (idealGroup == 0) {
                Group group = new Group();
                group.groupId = getNextCounterVal();
                List<String> latLongs = new ArrayList<String>();
                latLongs.add(latLng);
                group.latLongs = latLongs;
                group.time = time;
                createGroupId = group.groupId;
                tempGroups.put(group.groupId, group);
                groupList.add(group);
            }

            retVal = sendGroups(groupList, idealGroup, createGroupId);
        } else {
            Integer createGroupId = 0;
            Group group = new Group();
            group.groupId = getNextCounterVal();
            List<String> latLongs = new ArrayList<String>();
            latLongs.add(latLng);
            group.latLongs = latLongs;
            group.time = time;
            createGroupId = group.groupId;
            List<Group> newGroupList = new ArrayList<>();
            newGroupList.add(group);
            tempGroups.put(group.groupId, group);
            retVal = sendGroups(newGroupList, group.groupId, createGroupId);
        }

        return retVal;
    }

    private String sendGroups(List<Group> groupList, Integer idealGroupId, Integer createGroupId) {
        Response1 groupsResponse = new Response1();
        groupsResponse.noOfGroups = groupList.size();
        groupsResponse.grouplist = groupList;
        groupsResponse.ideal = idealGroupId;
        groupsResponse.create = createGroupId;

        return gson.toJson(groupsResponse);

    }

}
