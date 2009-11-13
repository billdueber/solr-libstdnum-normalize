package edu.umich.lib.hlb3;
/*******************************************
HLB3.java

- input (unnormalized call number),

- output: two sets of topics_set. 
(
pairs=Health Sciences | Dentistry |
pairs=Health Sciences | Internal Medicine |
pairs=Health Sciences | Kinesiology and Sports |
pairs=Health Sciences | Orthopaedic Surgery |
pairs=Health Sciences | Surgery |
pairs=Social Sciences | Kinesiology and Sports |

topics=Dentistry
topics=Health Sciences
topics=Internal Medicine
topics=Kinesiology and Sports
topics=Orthopaedic Surgery
topics=Social Sciences
topics=Surgery
)

How to call ...
ArrayList<String> pairs = HLB3.fullTopics(callnumber);
ArrayList<String> topics = HLB3.topicComponents(callnumber);

University of Michigan Library.
Young Hyun Park.
2009-10-13.
******************************************/

import java.io.*;
import java.util.*;
import java.util.regex.*;
import edu.umich.lib.normalizers.*;

public class HLB3 {
  
public static ArrayList<topic_obj> tlist = new ArrayList<topic_obj>();
public static ArrayList<range_obj> rlist = new ArrayList<range_obj>();
public static ArrayList<String> r_index = new ArrayList<String>();

//inner class topic_obj stores topics_set lists from call number json file.
class topic_obj {
int topic_id = 0;
String cat = null;
String subcat = null;
String subsubcat = null;

topic_obj(int t1,String t2,String t3,String t4) {
this.topic_id = t1;
this.cat = t2;
this.subcat = t3;
this.subsubcat = t4;
}

int get_tid() {
return this.topic_id;
}

String get_cat() {
return this.cat;
}

String get_subcat() {
return this.subcat;
}

String get_subsubcat() {
return this.subsubcat;
}
}

//inner class range_obj stores call number range data from call number json file.
class range_obj {
int range_id = 0;
String range_start = null;
String range_end = null;
int muid = 0;

range_obj(int t1,String t2,String t3,int t4) {
this.range_id = t1;
this.range_start = t2;
this.range_end = t3;
this.muid = t4;
}

int get_rid() {
return this.range_id;
}

String get_rstart() {
return this.range_start;
}

String get_rend() {
return this.range_end;
}

int get_muid() {
return this.muid;
}

}

// static initializer block is executed by the virutal machine when the class is loaded. This will json_parse function in call hlb3_json_parse class which will parse json file and create tlist, rlist, r_index arraylists.   
static {
hlb3_json_parse jp = new hlb3_json_parse();
jp.json_parse("hlb3.json");
}

public static ArrayList<String> fullTopics (String callnumber) {

Set<String> pairs_set = new TreeSet<String>();
ArrayList<String> pairs = new ArrayList<String>();
String temp3 = null;

try {
callnumber = callnumber.toUpperCase();

//String normalized = callno_func_java.callno_parse(callnumber,"0");
String normalized = LCCallNumberNormalizer.normalize(callnumber);   

/*
tlist arraylist: topic list array from json file.... [166,Social Sciences,Disability Studies,]....
rlist arraylist: call number ranges array from json file. ....[166,PS  648 H 23,PS  648 H 23,40119,PS]....
r_index arraylist: call number range start list. ....[RA 1055.50] [RC  376.50] [RC  451.40 H 35].... 
*/

//Converting ArrayList to Object array
Object[] elements = tlist.toArray();

// Sort call number range start list.
Collections.sort(r_index);

// Get the first char of input call number for example: RC570.4 => R.
String first_char = normalized.substring(0,1);

// Binary search for 'the first char of input call number' and 'input call number' from r_index (range start list from json file).  
int s_begin = Collections.binarySearch(r_index,first_char);
int s_end = Collections.binarySearch(r_index,normalized); 

s_begin = Math.abs(s_begin)-1;
s_end = Math.abs(s_end)-1;

if ((s_begin >= 0) && (s_end >= 0)) {
int trs = 0;
String temp1 = null;
String temp2 = null;
int n1 = 0;
int n2 =0;
Object[] relm = rlist.toArray();
for(int i=s_begin; i <= s_end ; i++)
{
	range_obj relem = (range_obj)relm[i];        

	temp1 = relem.get_rstart();
	temp2 = relem.get_rend();  

	n1 = normalized.compareTo(temp1);
	if (n1 >= 0) {
		n2 = normalized.compareTo(temp2);
		if (n2 <= 0) {
		trs = relem.get_rid();
		
		// lookup for topic arraylist based on range id.	
		for(int ii=0; ii < elements.length ; ii++) {
        		topic_obj sk = (topic_obj)elements[ii];
        		if (sk.get_tid() == trs) {
        			//System.out.println("topics_set="+sk.get_tid()+","+sk.get_cat()+","+sk.get_subcat()+","+sk.get_subsubcat());

				if (sk.get_subsubcat() == "") {
				temp3 = sk.get_cat()+" | "+sk.get_subcat();
				} else {	
        			temp3 = sk.get_cat()+" | "+sk.get_subcat()+" | "+sk.get_subsubcat();
        		}
				pairs_set.add(temp3);
     			}		
		}	
		} 
	}	
}
}

if (pairs_set.isEmpty()) {
    //return null;
} else {
pairs_set.remove("");

Iterator set1_list = pairs_set.iterator();
while (set1_list.hasNext()) {
   String tt = (String)set1_list.next();
   pairs.add(tt);
}
}
} catch(Exception e){e.printStackTrace();}

return pairs;
}

public static ArrayList<String> topicComponents (String callnumber) {

Set<String> topics_set = new TreeSet<String>();
ArrayList<String> topics = new ArrayList<String>();
String temp3 = null;

try {

callnumber = callnumber.toUpperCase();

//String normalized = callno_func_java.callno_parse(callnumber,"0");
String normalized = LCCallNumberNormalizer.normalize(callnumber);   

//Converting ArrayList to Object array
Object[] elements = tlist.toArray();

// Sort call number range start list.
Collections.sort(r_index);

// Get the first char of input call number for example: RC570.4 => R.
String first_char = normalized.substring(0,1);

// Binary search for 'the first char of input call number' and 'input call number' from r_index (range start list from json file).  
int s_begin = Collections.binarySearch(r_index,first_char);
int s_end = Collections.binarySearch(r_index,normalized); 

s_begin = Math.abs(s_begin)-1;
s_end = Math.abs(s_end)-1;

if ((s_begin >= 0) && (s_end >= 0)) {
int trs = 0;
String temp1 = null;
String temp2 = null;
int n1 = 0;
int n2 =0;
Object[] relm = rlist.toArray();
for(int i=s_begin; i <= s_end ; i++)
{
	range_obj relem = (range_obj)relm[i];        

	temp1 = relem.get_rstart();
	temp2 = relem.get_rend();  

	n1 = normalized.compareTo(temp1);
	if (n1 >= 0) {
		n2 = normalized.compareTo(temp2);
		if (n2 <= 0) {
		trs = relem.get_rid();
				
		// lookup for topic arraylist based on range id.	
		for(int ii=0; ii < elements.length ; ii++) {
        		topic_obj sk = (topic_obj)elements[ii];
        		if (sk.get_tid() == trs) {
        			//System.out.println("topics_set="+sk.get_tid()+","+sk.get_cat()+","+sk.get_subcat()+","+sk.get_subsubcat());

				if (sk.get_subsubcat() == "") {
				temp3 = sk.get_cat()+" | "+sk.get_subcat();
				} else {	
        			temp3 = sk.get_cat()+" | "+sk.get_subcat()+" | "+sk.get_subsubcat();
        		}
					topics_set.add(sk.get_cat());
        			topics_set.add(sk.get_subcat());
        			topics_set.add(sk.get_subsubcat());
        		}		
		}	
		} 
	}	
}
}

if (topics_set.isEmpty()) {
    //return null;
} else {
topics_set.remove("");

Iterator set2_list = topics_set.iterator();
while (set2_list.hasNext()) {
   String tt = (String)set2_list.next();
   topics.add(tt);
}
}
} catch(Exception e){e.printStackTrace();}

return topics;
}

 } // end of class


