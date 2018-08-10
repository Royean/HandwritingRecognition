package Demo.handwritingIdentify;

import java.util.HashMap;

public class Knn {
	private int nNeighbours;
	private KnnNode dist[];
	private int bucketLeft;
	
	public Knn(int n) {
		this.nNeighbours=n;
		dist=new KnnNode[n];
		bucketLeft=n;
	}
	
	public double distanceCal(int sample[][],int pixel[][],int dimens) {
		int sum=0;
		for(int i=0;i<dimens;i++) {
			for(int j=0;j<dimens;j++) {
				int s1=sample[i][j];
				int s2=pixel[i][j];
				sum+=(s1-s2)*(s1-s2);
			}
		}
		
		return Math.sqrt(sum);
	}
	
	private int index=0;
	public void sort(int sample[][],int pixel[][],int dimens,String firstCharacter) {
		KnnNode temp=new KnnNode(distanceCal(sample,pixel,dimens),firstCharacter);
		//首先，把k个槽先填满；
		if(bucketLeft>0) {
			dist[index++]=temp;
			bucketLeft--;
		}
		else{
			int flag=0;
			double max=0;
			for(int i=0;i<this.nNeighbours;i++) {
				if(dist[i].distance>max) {
					max=dist[i].distance;
					flag=i;
				}
			}
			if(max>temp.distance) dist[flag]=temp;
		}
		System.out.println(distanceCal(sample,pixel,dimens));
	}
	
	public String predict() {
		int max=0;
		String maxNumber="";
		HashMap<String, Integer> map=new HashMap<String,Integer>();  // K 为首字母，V 为出现字数
		for(int i=0;i<this.nNeighbours;i++) {
			map.put(dist[i].firstCharacter, 0);
		}
		for(int i=0;i<this.nNeighbours;i++) {
			String tmp=dist[i].firstCharacter;
			int val=map.get(tmp)+1;
			if(val>max) {
				max=val;
				maxNumber=tmp;
			}
			map.put(tmp, val);
		}
		//int count[]=new int[6];
//		for(int i=0;i<this.nNeighbours;i++) {
//			if(++count[dist[i].firstCharacter-'0']>max) {
//				maxNumber=dist[i].firstCharacter-'0';
//				max=count[dist[i].firstCharacter-'0'];
//			}
//		}
		for(int i=0;i<this.nNeighbours;i++) {
			System.out.print(dist[i].distance+" "+dist[i].firstCharacter+" ");
		}
		return maxNumber;
	}
	
}
