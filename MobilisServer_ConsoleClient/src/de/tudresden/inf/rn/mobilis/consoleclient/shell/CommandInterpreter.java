/*******************************************************************************
 * Copyright (C) 2010 Technische Universitšt Dresden
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Dresden, University of Technology, Faculty of Computer Science
 * Computer Networks Group: http://www.rn.inf.tu-dresden.de
 * mobilis project: http://mobilisplatform.sourceforge.net
 ******************************************************************************/

package de.tudresden.inf.rn.mobilis.consoleclient.shell;

import java.io.File;

import de.tudresden.inf.rn.mobilis.consoleclient.Controller;
import de.tudresden.inf.rn.mobilis.consoleclient.bean.TemplateBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.admin.RegisterServiceBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.coordination.StopServiceInstanceBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.deployment.PrepareServiceUploadBean;

/**
 * The Class CommandInterpreter interpretes the commands.
 */
public class CommandInterpreter {
	
	/** The _controller. */
	private Controller _controller;


	/**
	 * Instantiates a new command interpreter.
	 *
	 * @param controller the controller
	 */
	public CommandInterpreter(Controller controller) {
		_controller = controller;
	}
	
	
	/**
	 * Configure.
	 *
	 * @param id the id
	 */
	public void configure(String id){
		if(id.equals( "3")){
			_controller.getConnection().sendXMPPBean( 
					TemplateBean.createConfigureServiceBeanTreasureHuntService( _controller.getSettings().getMobilisAdminJid() )
			);
		}					
		else if(id.equals( "4")){
			_controller.getConnection().sendXMPPBean( 
					TemplateBean.createConfigureServiceBeanTreasureHunt2Service( _controller.getSettings().getMobilisAdminJid() )
			);
		}
		else if(id.equals( "5")){
			_controller.getConnection().sendXMPPBean( 
					TemplateBean.createConfigureServiceBeanXHuntService( _controller.getSettings().getMobilisAdminJid(), 2 )
			);
		}
		else if(id.equals( "6")){
			_controller.getConnection().sendXMPPBean( 
					TemplateBean.createConfigureServiceBeanXHuntService( _controller.getSettings().getMobilisAdminJid(), 3 )
			);
		}
	}
	
	/**
	 * Register command.
	 *
	 * @param namespace the namespace of service to register
	 * @param version the version of service to register
	 */
	public void register(String namespace, int version) {
		if( null == namespace || namespace.length() < 1 ){
			_controller.getLog().writeToConsole( "Invalid namespace." );
			
			return;
		}
		
		if( version < 1 ){
			_controller.getLog().writeToConsole( "Invalid version." );
			
			return;
		}
		
		RegisterServiceBean bean = new RegisterServiceBean( namespace, version );		

		bean.setTo(_controller.getSettings().getMobilisAdminJid());
		bean.setType(XMPPBean.TYPE_SET);
		
		_controller.getConnection().sendXMPPBean(bean);
	}
	
	/**
	 * Send file.
	 *
	 * @param filepath the filepath
	 */
	public void sendFile(String filepath){
		File file = new File(filepath);
		
		if(file.exists()){
			boolean success = _controller.getConnection().transmitFile( 
					file,
					"",
					_controller.getSettings().getMobilisDeploymentJid() );
			
			if( success )
				_controller.getLog().writeToConsole( "Filetransfer successful" );
			else
				_controller.getLog().writeErrorToConsole( "Filetransfer unsuccessful" );
		}
		else
			_controller.getLog().writeToConsole( "No such file was found." );		
	}
	
	/**
	 * Send prepare file.
	 *
	 * @param filename the filename
	 */
	public void sendPrepareFile(String filename){
		PrepareServiceUploadBean bean = new PrepareServiceUploadBean(filename);
		bean.setTo( _controller.getSettings().getMobilisDeploymentJid() );
		bean.setType( XMPPBean.TYPE_SET );
		
		_controller.getConnection().sendXMPPBean( bean );
	}
	
	/**
	 * Stop service.
	 *
	 * @param jid the jid of the service instance
	 */
	public void stopService(String jid){
		StopServiceInstanceBean bean = new StopServiceInstanceBean( jid );
		bean.setType( XMPPBean.TYPE_SET );
		bean.setTo( _controller.getSettings().getMobilisCoordinatorJid() );
		
		_controller.getConnection()
			.sendXMPPBean( bean );
	}

}
