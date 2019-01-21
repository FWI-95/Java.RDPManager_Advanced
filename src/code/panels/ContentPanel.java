package code.panels;

import javax.swing.JPanel;

import Interface.global;
import code.GUI.Window;
import code.main.Engine;
import code.source.Group;
import code.source.Profile;
import code.source.Server;
import code.source.Settings;

public class ContentPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	Engine engine;
	Window window;
	Settings settings;
	
	GroupPanel panelGroup;
	ServerPanel panelServer;
	InfoPanel panelInfo;

	public Profile CurrentProfile;
	public Group CurrentGroup;
	public Server CurrentServer;
	
	public ContentPanel(Window parWindow) {
		window = parWindow;
		engine = window.getEngine();
		settings = engine.getSettings();
		
		this.setLayout(null);
		this.setBounds(0, 0, global.Width, global.Height);
		
		panelGroup = new GroupPanel(engine, this);
		panelServer = new ServerPanel(engine, this);
		panelInfo = new InfoPanel(engine, this);
		
		this.add(panelGroup);
		this.add(panelServer);
		this.add(panelInfo);		
		
//		this.repaint();

	}
	
	public void SetCurrentGroup(Group parGroup) {
		CurrentGroup = parGroup;
		PushCurrent();
	}
	
	public void SetCurrentServer(Server parServer) {
		CurrentServer = parServer;
		PushCurrent();
	}
	
	public void PushCurrent() {
		engine.SetCurrent(CurrentGroup,CurrentServer);
	}
	
	public void PullCurrent() {
		CurrentGroup = engine.getProfileManager().getCurrentProfile().getCurrentGroup();
		if(CurrentGroup != null) {
			CurrentServer = CurrentGroup.GetCurrentServer();
		}else {
			CurrentServer = null;
		}
	}
	
	public void NewGroup() {
		String name = "";
		name = engine.input("Wie soll die Gruppe hei�en?");
		if(name != "" && name != null) {
			engine.addGroup(name);
		}
		panelGroup.Update();
	}
	
	public void DeleteGroup() {
		if(engine.confirm("M�chten Sie die Gruppe " + CurrentGroup.GetCaption() + " mit allen Server l�schen?")) {
			engine.deleteGroup(CurrentGroup.GetID());
		}
	}
	
	public void NewServer() {
		if(CurrentGroup != null) {
			
			PushCurrent();
			
//			engine.debug("Profile: " + CurrentProfile.getID() + " - " + CurrentProfile.getCaption());
			engine.debug("Gruppe: " + CurrentGroup.GetID() + " - " + CurrentGroup.GetCaption());

			engine.getProfileManager().addServer(engine.GetNextServerID(),
							"",
							"Neuer Server",
							CurrentGroup.GetID(),
							"",
							"",
							false,
							false,
							false,
							false,
							false,
							0,
							0); 
			
			UpdateComponents();
			
		}else {
			engine.log("Creating Server failed because no group is selected.");
		}
	}
	
	public void DeleteServer() {
		if(engine.confirm("M�chten Sie den Server " + CurrentServer.getCaption() + " wirklich l�schen?")) {
			engine.deleteServer(CurrentServer.getID());
		}
	}
	
	public void SaveServer() {
		if(CurrentServer != null) {
			if(engine.confirm("M�chten Sie die �nderungen speichern?")){
				int tmpSelectedGroup = panelGroup.getSelectedIndex();
				int tmpSelectedServer = panelServer.getSelectedIndex();
				
				engine.deleteServer(CurrentServer.getID());
				engine.AddServer(CurrentServer);
				
				panelGroup.setSelectedIndex(tmpSelectedGroup);
				panelServer.setSelectedIndex(tmpSelectedServer);
			}
		}
	}	
	
	public void Connect() {
		if(engine.confirm("M�chten Sie sich mit dem Server " + CurrentServer.getCaption() + " verbinden?")) {
			CurrentServer.connect();
		}
	}
	
	public void UpdateComponents() {
		UpdateGroups();
		UpdateServer();
		UpdateInfo();
	}
	
	public void UpdateGroups() {
		panelGroup.Update();
	}
	
	public void UpdateServer() {
		panelServer.Update();
	}
	
	public void UpdateInfo() {
		panelInfo.UpdateInfo();
	}

}
