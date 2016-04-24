package com.binaryworkspace.rcp.wwj.services;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

/**
 * Creates an instance of a {@link WWJService}.
 * <p>
 * This factory is registered as an extension in the plugin.xml file and is
 * registered as part of the RCP initialization lifecycle, but does not create
 * the actual service until it is needed (e.g. lazy loading).
 * 
 * <pre>
 *    {@literal <}extension
 *          point="org.eclipse.ui.services"{@literal >}
 *       {@literal <}serviceFactory
 *             factoryClass="x.rcp.services.WWJServiceFactory"{@literal >}
 *          {@literal <}service
 *                serviceClass="x.rcp.services.WWJService"{@literal >}{@literal <}/service{@literal >}
 *       {@literal <}/serviceFactory{@literal >}  
 *    {@literal <}/extension{@literal >}
 * </pre>
 * 
 * Once the RCP initialization is completed, {@link WWJService} can be
 * accessed by calling:
 * <tt>ServiceProvider.getService(WWJService.class);</tt>
 * 
 * @author Chris Ludka
 */
public class WWJServiceFactory extends AbstractServiceFactory {

	@Override
	public Object create(@SuppressWarnings("rawtypes") Class serviceInterface, IServiceLocator parentLocator, IServiceLocator locator) {
		return new WWJService();
	}
}