package Demo.handwritingIdentify;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UID;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class Listener extends MouseAdapter implements ActionListener {
	private Main mm;
	private Graphics2D g;
	private int[][] pixel = new int[40][40];
	private JComboBox<String> cbItem;
	private Knn knn;
	public Listener(Main mm, JComboBox<String> cbItem) {
		this.mm = mm;
		this.cbItem = cbItem;
		g = (Graphics2D) mm.getGraphics();
	}

	private int[][] sample = new int[40][40];

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		for(int i=0;i<40;i++) {
//			for(int j=0;j<40;j++) {
//				System.out.print(pixel[i][j]+" ");
//			}
//			System.out.println("");
//		}
		if (e.getActionCommand().equals("Save this sample")) {
			String selectedNumber=cbItem.getSelectedItem().toString();
			UID id=new UID();
			String rootPath="C:\\Users\\DearYou\\eclipse-workspace\\GUI\\src\\Demo\\handwritingIdentify\\TrainingData\\";
			String fileName=selectedNumber+"-"+id.hashCode();
			String absoluteFile=rootPath+fileName+".txt";
			File file=new File(absoluteFile);
			try {
				if(!file.exists())
					file.createNewFile();
				FileWriter out = new FileWriter(file);
				for(int i=0;i<40;i++) {
					for(int j=0;j<40;j++) {
						out.write(pixel[i][j]+"");
					}
				}
				out.flush();
				out.close();
			}catch(Exception e1) {
				e1.printStackTrace();
			}
			mm.repaint();
		} else if (e.getActionCommand().equals("Identify")) {
			// 识别结果；
			knn=new Knn(3);
			File fileDir=new File("C:\\Users\\DearYou\\eclipse-workspace\\GUI\\src\\Demo\\handwritingIdentify\\TrainingData\\");
			String[] fileList=fileDir.list();
			for(int i=0;i<fileList.length;i++) {
				File file=new File("C:\\Users\\DearYou\\eclipse-workspace\\GUI\\src\\Demo\\handwritingIdentify\\TrainingData\\"+fileList[i]);
				String number=file.getName().substring(0, 1);
				FileReader in;
				try {
					in = new FileReader(file);
					for(int j=0;j<40;j++){
						for(int k=0;k<40;k++) {
							sample[j][k]=in.read()-'0';  //逐单位汉字/字母/数字读取！！！
							//System.out.print(sample[j][k]+" ");
						}
					//	System.out.println("");
					}
					in.close();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e1) {
                    e1.printStackTrace();
                }
			//开始进行 欧拉距离和KNN 运算；
				knn.sort(sample, pixel, 40, number);
			}
			JOptionPane.showMessageDialog(mm,"预测数字为："+knn.predict());
			mm.repaint();
		}
	}
	
	private int x1, y1, x2, y2;

	public void pixelReset() {
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 40; j++) {
				pixel[i][j] = 0;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
		pixelReset();
	}

	public void mouseDragged(MouseEvent e) {
		g.setColor(Color.WHITE);
		// g.setStroke(new BasicStroke(20));
		x2 = e.getX();
		y2 = e.getY();
		g.fillRect(x2, y2, 20, 20);
		//怎么会产生倒置的情况
		for (int i = x2; i < x2 + 20; i++) {
			for (int j = y2; j < y2 + 20; j++) {
					pixel[i/10][j/10] = 1;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		// 将数组导入文档

		/*
		 * int height=mm.getHeight(); int width=mm.getWidth(); Image
		 * image=mm.createImage(width, height);
		 * 
		 * g.drawImage(image1, 0, 0, width, height, null); for(int i=0;i<40;i++) {
		 * for(int j=0;j<30;j++) { // 为什么得到的rgb最后比0xffffff大 1 ？ if(image1.getRGB(10*i,
		 * 10*j)==0x1000000) { pixel[i][j]=1; } System.out.println(image1.getRGB(1, 1));
		 * } }
		 */

	}
}
