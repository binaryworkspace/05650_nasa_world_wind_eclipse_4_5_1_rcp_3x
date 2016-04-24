package com.binaryworkspace.rcp.wwj.compositions;

/**
 * Used to group or manage a set of widgets.
 * <p>
 * Typical use cases may include:
 * <ul>
 * <li>Abstracting widgets with generic model interfaces such that they can be
 * reused under multiple use cases (e.g. generic table or tree models).</li>
 * <li>A helper class used to manage complex or repetitive layouts.</li>
 * <ul>
 * 
 * @author Chris Ludka
 * 
 */
public interface IComposition {

	/**
	 * Initialization logic that is intended to be called post composition
	 * construction.
	 * <p>
	 * This initialization method can be utilized to support several design
	 * patterns to facilitate:
	 * <ul>
	 * <li>Instantiation a composition's widgets. Developers may wish to adhere
	 * to a strict rule that during construction only internal variables are
	 * configured. Widget construction therefore would occur in the init() call.
	 * This might be useful when other dependencies and services may need to be
	 * bootstrapped before widgets are created or simply out of a sense of
	 * composition life cycle delineations.</li>
	 * <li>Setting of a composition's initial state. Developers may wish to
	 * instantiate widgets during construction but delay initializing the widget
	 * state until later in the bootstrap. Likewise, developers may wish to use
	 * the init() call to have greater control regarding when model listeners
	 * are registered or when specific handlers are attached to widgets.</li>
	 * <li>All or none of the above.</li>
	 * </ul>
	 */
	public void init();

	/**
	 * Performs any cleanup that may be necessary at the end of the
	 * composition's life cycle. This may include but is not limited to
	 * unregistering model listeners and freeing any necessary resources used by
	 * the composition.
	 */
	public void dispose();

}