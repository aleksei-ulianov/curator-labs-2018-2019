
package TaskServer;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 *
 * @author Nikita
 */
public class Alert  {

    
    List<Date> alarmTimeList = new ArrayList<>();
    
    void recieveTime(Journal jObject)throws ParseException{
        alarmTimeList=jObject.scanDate();              
    }
    
    boolean alarm(Journal jObject){         
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date currentTime = new Date();      
        for(Date date : alarmTimeList){
            if (sdf.format(date).equals(sdf.format(currentTime))){             
                return true;
            }   
        }
        return false;
    }
   
}
