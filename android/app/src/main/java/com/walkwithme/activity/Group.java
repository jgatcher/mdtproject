package com.walkwithme.activity;

import java.util.List;

/**
 * Created by User on 4/28/2015.
 */
public class Group {
  // {"noOfGroups":1,"ideal":1,"grouplist":[{"groupId":2,"latLongs":["30,40","50,60"]},{"groupId":1,"latLongs":["10,20","20,30"]}]}
  //{"noOfGroups":1,"ideal":1,"create":1,"grouplist":[{"groupId":1,"latLongs":["40.437861,-79.924734"],"time":"7"}]}
  String groupId;
  String time;
  List<String> latLongs;
}
