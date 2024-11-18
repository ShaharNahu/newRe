import java.util.ArrayList;

public class ThreadCheckArray implements Runnable 
{
    private boolean flag;
    private boolean[] winArray;
    SharedData sd;
    ArrayList<Integer> array; // Updated from int[] to ArrayList<Integer>
    int b;

    public ThreadCheckArray(SharedData sd) 
    {
        this.sd = sd;    
        synchronized (sd) 
        {
            array = sd.getArray(); // Updated to get ArrayList<Integer>
            b = sd.getB();
        }        
        winArray = new boolean[array.size()]; // Updated to use array.size() instead of array.length
    }

    void rec(int n, int b)
    {
        synchronized (sd) 
        {
            if (sd.getFlag())
                return;
        }    
        if (n == 1)
        {
            if (b == 0 || b == array.get(n - 1)) // Updated to use array.get()
            {
                flag = true;
                synchronized (sd) 
                {
                    sd.setFlag(true);
                }            
            }
            if (b == array.get(n - 1)) // Updated to use array.get()
                winArray[n - 1] = true;
            return;
        }
        
        rec(n - 1, b - array.get(n - 1)); // Updated to use array.get()
        if (flag)
            winArray[n - 1] = true;
        synchronized (sd) 
        {
            if (sd.getFlag())
                return;
        }    
        rec(n - 1, b);
    }

    public void run() {
        if (array.size() != 1) // Updated to use array.size() instead of array.length
            if (Thread.currentThread().getName().equals("thread1"))
                rec(array.size() - 1, b - array.get(array.size() - 1)); // Updated to use array.get()
            else 
                rec(array.size() - 1, b); // Updated to use array.size()
        if (array.size() == 1) // Updated to use array.size()
            if (b == array.get(0) && !flag) // Updated to use array.get()
            {
                winArray[0] = true;
                flag = true;
                synchronized (sd) 
                {
                    sd.setFlag(true);
                }
            }
        if (flag)
        {
            if (Thread.currentThread().getName().equals("thread1"))
                winArray[array.size() - 1] = true; // Updated to use array.size()
            synchronized (sd) 
            {
                sd.setWinArray(winArray);
            }    
        }
    }
}

