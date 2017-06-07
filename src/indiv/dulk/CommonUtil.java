package indiv.dulk;

/**
 * CommonUtil.
 *
 * @author Dulk
 * @version 20170606
 * @date 17-6-6
 */
public class CommonUtil {

    public static int[] sort(int[] arr) {
        int i, j, max, len, temp;
    	len = arr.length;
    	
    	for(i = len - 1; i > 0; i--){
    		max = i;
	    	for(j = 0; j < i; j++){
	    		if(arr[max] < arr[j]){
	    			temp = arr[max];
	    			arr[max] = arr[j];
	    			arr[j] = temp;
	    		}
	    	}
    	}

        return arr;
    }
    
}
