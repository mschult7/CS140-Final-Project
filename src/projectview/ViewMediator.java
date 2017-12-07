package projectview;

import java.util.Observable;

import javax.swing.JFrame;

import project.MachineModel;

public class ViewMediator extends Observable {
	
	private MachineModel model;
	private JFrame frame;
	public void step() { } 
	
	public MachineModel getModel() {
		return this.model;
	}
	public void setModel(MachineModel model) {
		this.model = model;
	}
	public JFrame getFrame() {
		return this.frame;
	}
}
