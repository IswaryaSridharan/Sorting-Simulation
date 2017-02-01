import java.util.*;
import java.util.Random;
import java.util.HashMap;
import java.io.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Event;

public class SortingSimulation extends java.applet.Applet implements Runnable
{
    private Thread sortThread;
    int[] dataSet1;
    int indexi=-1, indexj=-1;
    private SortingMethods sortMethods;
    private String type;

    public void init() {
        type = getParameter("type");
        setBackground(Color.white);
        resize(256, 256);
    }

    private void sort() {
        repaint();
        sortThread = new Thread(this);
        sortThread.start();
    }

    public boolean mouseUp(Event evt, int i, int j) {
        sort();
        return true;
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        int[] temp1 = dataSet1;
        int y = size().width - 1;

        g.setColor(Color.white);

        int i = temp1.length;
        while (--i >= 0)
        {
            g.drawLine(y, temp1[i], y, size().height);
            y-=2;
        }

        g.setColor(Color.black);
        y = size().width - 1;

        i = temp1.length;
        while (--i >= 0)
        {
            g.drawLine(y, 0, y, temp1[i]);
            y-=2;
        } 

        if (indexi >= 0) {
            g.setColor(Color.yellow);
            y = indexi * 2 + 1;
            g.drawLine(y, 0, y, size().height);
        }
        if (indexj >= 0) {
            g.setColor(Color.green);
            y = indexj * 2 + 1;
            g.drawLine(y, 0, y, size().height);
        }
    }

    void refresh(int i, int j) {
        indexi = i;
        indexj = j;     
        if (sortThread != null) {
            repaint();
        }
        try {
            Thread.sleep(16);
        } catch (Exception e) {
        }
    }

    void refresh() {
        refresh(indexi, indexj);
    }

    public void run() {
        try {
            sortMethods = new SortingMethods(this); 
            dataSet1 = sortMethods.generateRandomNumbers();
            int []temp1 = Arrays.copyOf(dataSet1, dataSet1.length);

            if (type.equals("bubble"))
            sortMethods.bubbleSort(dataSet1);

            if (type.equals("insert"))
            sortMethods.insertionSort(dataSet1);

            if (type.equals("select"))
            sortMethods.selectionSort(dataSet1);

            if (type.equals("quick"))
            sortMethods.quickSort(dataSet1);

            if (type.equals("merge"))
            sortMethods.mergeSort(dataSet1);
            
        } catch (Exception e) {
            throw new RuntimeException("Error during sorting");
        }
    }
    
    public int width()
    {
        return size().width;
    }

    public int height()
    {
        return size().height;
    }
}

class SortingMethods
{
        private SortingSimulation simulationInstance;
        public SortingMethods(SortingSimulation obj)
        {
	    this.simulationInstance = obj;
        }

	Random random = new Random();

	void bubbleSort(int []elements)
	{
		for(int i=0; i<elements.length; i++)
		{
			for(int j=0; j<elements.length-1; j++)
			{
				if(elements[j] > elements[j+1])
				{
					int temp = elements[j];
					elements[j] = elements[j+1];
					elements[j+1] = temp; 
				}
                                simulationInstance.refresh(i, j);
			}
		}
	}

	void insertionSort(int []elements)
	{
		int comp;
		for(int i=1; i<elements.length;i++)
		{
			comp = elements[i];
			int j = i-1;
			while((j >= 0) && (comp < elements[j]))
			{
				elements[j+1] = elements[j];
				j--;
                                simulationInstance.refresh(i, j);
			}
			elements[j+1] = comp;
                        simulationInstance.refresh(i, j);
		}
	}

	void selectionSort(int[]elements)
	{
		int count = 0;
		for(int i=0; i<elements.length; i++)
		{
			int min = 99999999;
			int index = -1;
			for(int j=count;j<elements.length; j++)
			{
				if(min>elements[j])
				{
					min = elements[j];
					index = j;
				}
                                simulationInstance.refresh(i, j);
			}
			count++;
			int temp = elements[i];
			elements[i] = min;
			elements[index] = temp;
		}
	}

	 void quickSortRecursion(int[]elements, int start, int end)
	{
                  int pivot;
                simulationInstance.refresh(start, end);
		  pivot = quickPartition(elements,start,end);
		

                 if(start < pivot -1) {
                    quickSortRecursion(elements, start, pivot-1);
                 }
     
    
                 if(end > pivot) {
                    quickSortRecursion(elements, pivot, end);
                 }
	}

	int quickPartition(int[]elements, int start, int end)
	{

            int pivot = elements[start];
            while(start <= end) {
                while(elements[start] < pivot)
                   start++;

                while(elements[end] > pivot)
                   end--;
         
        // swap the values which are left by lower and upper bounds 
        if(start <= end) {
            int tmp = elements[start];
            elements[start]  = elements[end];
            elements[end] = tmp;        
            start++;
            end--;
                simulationInstance.refresh();
         }
              }   
             return start;
	}
	void quickSort(int[]elements)
	{
		int start = 0;
		int end = elements.length - 1;
		quickSortRecursion(elements,start,end);
                simulationInstance.refresh();
	}

	void mergeSortRecursion(int[]elements, int[] copyArray, int start, int end)
	{
                simulationInstance.refresh(start, end);
		int mid = (start + end)/2;
		if(start < end)
		{
			mergeSortRecursion(elements, copyArray, start,mid);
			mergeSortRecursion(elements,copyArray, mid+1,end);
			mergeElements(elements, copyArray, start, mid, end);
		}
	}

	void mergeElements(int[]elements, int[] copyArray, int start, int mid, int end)
	{
		int i, j, k;
		k = 0;
		i = start;
		j = mid+1;
		while(i <= mid && j <= end)
		{
			if(elements[i] < elements[j])
			{
				copyArray[k++] = elements[i++];       
			}
			else
			{
				copyArray[k++] = elements[j++];
			}
		}
 
		while(i <= mid)
		{
			copyArray[k++] = elements[i++];
		}
 
		while(j <= end)
		{
			copyArray[k++] = elements[j++];
		}
 
		for(i=end; i >= start; i--)
		{
			elements[i] = copyArray[--k];        // copying back the sorted list to a[]
                        simulationInstance.refresh();
		} 
	}

	void mergeSort(int[]elements)
	{
		int start = 0;
		int end = elements.length - 1;
		int copyArray[] = new int[elements.length];
		mergeSortRecursion(elements, copyArray, start,end);
	}

        public int[] generateRandomNumbers() {
            int[] array = new int[simulationInstance.width() / 2];
            int scalingFactor = simulationInstance.height() / array.length;
            for (int i = 0; i < array.length; i++) {
                array[i] = i * scalingFactor;
            }
   
            for (int i = 0; i < array.length; i++) {
                int j = (int) (i * Math.random());
                int t = array[i];
                array[i] = array[j];
                array[j] = t;
            }
            return array;
        }
}
