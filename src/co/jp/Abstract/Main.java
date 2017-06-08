package co.jp.Abstract;

import java.util.HashMap;
import java.util.Map;



public class Main {

	public static void main(String[] args) {
		// using it as a driver
		System.out.println("let me hear your voice");
		System.out.println("hey deng chao wo");
		
		int ret = 0;
	    Main main = new Main();
	    ret = main.execute(args);
	    
	    System.exit(ret);
	}
	
	private int execute(String[] args)
	  {
	    int ret = 0;
	    String batchClassId = null;
	    String className = null;
	    AbstractPublic batch = null;
	    Map<String, String> argsMap = null;
	    try
	    {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Startup parameters: ");
	        int i = 0;
	        for (String parm : args)
	        {
	          sb.append("[");
	          sb.append(Integer.toString(i));
	          sb.append("]");
	          sb.append(":");
	          sb.append(parm);
	          sb.append(" ");
	          i++;
	        }
	        System.out.println(sb.toString());
	      
	      argsMap = splitArgs(args);
	      
	      batchClassId = (String)argsMap.get("CLASSNAME");
	      // should use xml or properties file to get prefix
	      className = "co.jp.batch." + batchClassId;
	      
	      System.out.println("ClassName:" + batchClassId);
	      Class<?> batchClass = Class.forName(className);
	      
	      Object batchobj = batchClass.newInstance();
	      if ((batchobj instanceof AbstractPublic))
	      {
	        batch = (AbstractPublic)batchobj;
	      }
	      else
	      {
	        System.out.println("the specified class is not a instance of AbstractPublic class.");
	        return 10;
	      }
	    }
	    catch (ClassNotFoundException e)
	    {
	    	System.out.println("can't find the specified class.");
	    	ret = 10;
	    }
	    catch (Exception e)
	    {
	      System.out.println("unknown error occured.");
	      e.printStackTrace();
	      ret = 10;
	    }
	   
	    if (ret != 10) {
	      try
	      {
	        ret = batch.absExecute(argsMap);
	      }
	      catch (Exception e)
	      {
	        ret = 10;
	      }
	    }
	    return ret;
	  }
	
	private Map<String, String> splitArgs(String[] splitargs)
	  {
	    Map<String, String> argsMap = new HashMap<String, String>();
	    try
	    {
	      for (int i = 0; i < splitargs.length; i++) {
	        if (i == 0)
	        {
	          argsMap.put("CLASSNAME", splitargs[0]);
	        }
	        else
	        {
	          String[] strargs = splitargs[i].split(":", 0);
	          argsMap.put(strargs[0], strargs[1]);
	        }
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return argsMap;
	  }

}
// CodeCheck  ver1.1.10: c8378331a3904d8f2737e13ac28eed8f9dcae975de14d5d905c6e33a5020e151