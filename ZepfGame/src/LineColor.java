
public class LineColor {
	
	public float r, g, b;
	
	public LineColor(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
		
	}
	
	public static LineColor[] getListOfColors(){
		LineColor[] colorList = new LineColor[10];
		
		colorList[0] = new LineColor(1,0,0);
		colorList[1] = new LineColor(1,(float)0.647,0);
		colorList[2] = new LineColor(1,1,0);
		colorList[3] = new LineColor(1,0,0);
		colorList[4] = new LineColor(1,0,1);
		colorList[5] = new LineColor((float)0.502,0,(float)0.502);
		colorList[6] = new LineColor(0,(float)0.502,0);
		colorList[7] = new LineColor(0,1,1);
		colorList[8] = new LineColor(0,0,1);
		colorList[9] = new LineColor((float)0.502,0,0);
		
		return colorList;
	}

}
