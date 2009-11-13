package edu.umich.lib.hlb3;
/***********************************
hlb3_json_parse.java

call number json file parser.

- input (json file name) 
- output (tlist:topic list, rlist: call number range list, r_index: call number range start list).

Young Hyun Park.
2009-10-13.
*************************************/

import java.io.*;
import java.util.*;
import java.util.regex.*;
import edu.umich.lib.normalizers.*;
import java.net.*;
public class hlb3_json_parse extends HLB3 {

//Comparator for sorting topic list based on topic id.
class TopicidComparator implements Comparator{
public int compare(Object emp1, Object emp2){

int emp1tid = ((topic_obj) emp1).get_tid();
int emp2tid = ((topic_obj) emp2).get_tid();

if( emp1tid > emp2tid )
return 1;
else if( emp1tid < emp2tid )
return -1;
else
return 0;
}
}

// Comparator for sorting call number range list based on call number range start.
class RangeComparator implements Comparator{
public int compare(Object emp1, Object emp2){

String emp1range = ((range_obj)emp1).get_rstart();
String emp2range = ((range_obj)emp2).get_rstart();
return emp1range.compareTo(emp2range);
}
}


  public void json_parse(String json_file) {
    try {
	BufferedReader in; 
	String in_str = "";
	String mcall_type = "";
	String table_name = "";
	char temp_char1;
	int temp_int1 =0;
	int r_open = 11;
	int r_close = 11;
	int loop_count =0;
	
	/* Input elements for jason_call_ranges table */
	int range_id =0;
	String range_start = "";
	String range_end = "";
	int uid =0;

	/* Input elements for jason_title table */  
	int topic_id =0;
	String cat = "";
	String subcat = "";
	String subsubcat = "";	

	try {
      
	    //       	in = new BufferedReader(new FileReader(json_file));   // open jason file.  
	    URL url = new URL("http://mirlyn.lib.umich.edu/static/hlb3/hlb3.json");
	    in = new BufferedReader(
				    new InputStreamReader(
							  url.openStream()));
	    
        while ((in_str = in.readLine()) != null) {
 	in_str = in_str.trim(); 
	//System.out.println ("------------------------");
	//System.out.println ("input="+in_str+","+in_str.length());	

	if (in_str.length() > 0) {	
		temp_char1 = in_str.charAt(0);
		
		if(in_str.indexOf("dranges") > 0) { // dewey call number case.
			mcall_type = "1";
			table_name = "jason_call_ranges_yp";	
			//System.out.println ("in_str="+in_str+","+mcall_type+","+table_name);  
		}
		else if (in_str.indexOf("lcranges") > 0) {   // LC call number case.
                	mcall_type = "0";       
			table_name = "jason_call_ranges_yp"; 
                	//System.out.println ("in_str="+in_str+","+mcall_type+","+table_name);
                }
		else if (in_str.indexOf("topics") > 0) {  // Topic lists.
                	mcall_type = "t";               
			table_name="jason_topics_yp";  
                	//System.out.println ("in_str="+in_str+","+mcall_type+","+table_name);    
               	} else if (in_str.indexOf(": [") >= 0) { 
			if (mcall_type == "t") {
			temp_int1 = in_str.lastIndexOf("\"");
                        in_str = in_str.substring(1,temp_int1);
			topic_id = Integer.parseInt(in_str); 
			}	
		} else if (in_str.indexOf('[') >= 0) {
			r_open = 22;
			r_close = 11;
			loop_count = 0;	
		} else if (in_str.indexOf(']') >= 0) {
			//System.out.println ("begin00="+r_open+","+r_close+","+mcall_type);	
			if (r_open == 22) {	
			//if ((mcall_type == "0") || (mcall_type == "1")) {
			if (mcall_type == "0") { //Deal with the only LC case.		       	

			range_start = range_start.toUpperCase();
			range_end = range_end.toUpperCase();

			//Call call number normalizer. 
			range_start = LCCallNumberNormalizer.normalize(range_start);
			range_end = LCCallNumberNormalizer.normalize(range_end);	
			
			//range_start = callno_func_java.callno_parse(range_start,"0");
                        //range_end = callno_func_java.callno_parse(range_end,"0");

			//System.out.println ("range_id="+range_id+","+range_start+","+range_end+","+uid+","+mcall_type+","+stem);
		
			// add call number range information from json file into rlist arraylist.	
			range_obj rob = new range_obj(range_id,range_start,range_end,uid);
			rlist.add(rob);  

			// add range start into r_index arraylist.
			r_index.add(range_start);
	
			range_id =0;
                        range_start = "";
                        range_end = "";
                        uid = 0;
			} else if (mcall_type == "t") {
			//System.out.println ("topic_id="+topic_id+","+cat+","+subcat+","+subsubcat);
		
			// add topic list information from json file into tlist arraylist.
			topic_obj top_obj = new topic_obj(topic_id,cat,subcat,subsubcat);	
			tlist.add(top_obj);	
 
			cat = "";
                        subcat = "";  
                        subsubcat = "";	
			loop_count = 0;	
			}
			
			//System.out.println ("------------------------");
			r_open = 11;
                        r_close = 22;	
			}	
		} else {
			if (temp_char1 == '\"') {  // only deal with starting with double-quote ("). for example "Humanities".	
			loop_count = loop_count + 1;
			temp_int1 = in_str.lastIndexOf("\"");
                        in_str = in_str.substring(1,temp_int1);	
			
			if ((mcall_type == "0") || (mcall_type == "1")) {
				if (loop_count == 1) {
                                        range_id = Integer.parseInt(in_str);
				} else if (loop_count == 2) {
					range_start = in_str;
                                }  else if (loop_count == 3) {
					range_end = in_str;
                                }  else if (loop_count == 4) {
					if (mcall_type == "1") {
						uid = 0;
					} else {	
						uid = Integer.parseInt(in_str);
                               		} 
				}  else if (loop_count == 5) {
					mcall_type = in_str;
                                } 
			} else if (mcall_type == "t") {
                                if (loop_count == 1) {
                                       cat  = in_str;
                                } else if (loop_count == 2) {
                                        subcat = in_str;
                                } else if (loop_count == 3) {
                                        subsubcat = in_str;
                                }
			}
			}
		}
	}   // end of  if (in_str.length() > 0) 	
	}   // end of  while ((in_str = in.readLine()) != null)  

	/* sort topic array by topic_id, sort call number range array by range_start. */
	TopicidComparator tcom = new TopicidComparator();
	RangeComparator rcom = new  RangeComparator();
	Collections.sort(tlist, tcom);
	Collections.sort(rlist, rcom);
	

	in.close();
    	} catch (IOException e) {
    	}
   } catch(Exception e){e.printStackTrace();}
 }
}


/*******************************
JASON FILE SAMPLE EXAMPLE.
{
dewey call number type ->   "dranges" : [
begin ->      [
         "35",
         "350.000",
         "350.999"
end ->      ],
begin ->      [
         "70",
         "99.999",
         "99.999"
end ->      ],

end ->    ],   check this end had begin before, if not skip it. 
LC call type ->   "lcranges" : [
begin ->      [
         "166",
         "nx652h35",
         "nx652h35",
         "40124"
end ->      ],
begin ->      [
         "166",
         "pn1590h36",
         "pn1590h36",
         "40123"
end ->      ],
begin ->        [
         "166",
         "pn1992.80h36",
         "pn1992.80h36",
         "40122"
end ->      ],
end ->     ],
-------------------------------------------------insert into mdeveloper.jason_call_ranges_yp table in oracle.
topic table starts ->  "topics" : {
get topic id check for (": [") ->      "100" : [
begin ->         [
            "Business",
            "Marketing"
end ->         ]
end ->      ],  check this end had begin before, if not skip it. 
get topic id check for (": [") ->      "101" : [
begin ->         [
            "Social Sciences",
            "Communication Studies"
end ->         ]
end ->      ],
	 "154" : [
         [
            "Science",
            "Space Sciences"
         ],
         [
            "Engineering",
            "Space Sciences"
         ]
      ],
get topic id check for (": [") ->      "99" : [
begin ->         [
            "Business",
            "Management"
end ->         ]
end ->      ] check this end had begin before, if not skip it.   
----------------------------------------------insert into mdeveloper.jason_topics_yp oracle table in oracle. 
   }
}


*********************************/
