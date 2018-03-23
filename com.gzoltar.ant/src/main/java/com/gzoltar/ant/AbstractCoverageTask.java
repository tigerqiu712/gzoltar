package com.gzoltar.ant;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import com.gzoltar.agent.AgentJar;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.AgentOutput;

/**
 * Base class for all coverage tasks that require agent options
 */
public class AbstractCoverageTask extends Task {

  private final AgentConfigs agentConfigs;

  private boolean enabled;

  private File destfile;

  /**
   * Create default agent options
   */
  public AbstractCoverageTask() {
    super();
    this.agentConfigs = new AgentConfigs();
    this.destfile = new File(AgentConfigs.DEFAULT_DESTFILE);
    this.enabled = true;
  }

  /**
   * @return Whether or not the current task is enabled
   */
  public boolean isEnabled() {
    return this.enabled;
  }

  /**
   * Sets whether or not the current task is enabled
   * 
   * @param enabled Enablement state of the task
   */
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Sets the location to write coverage execution data to. Default is <code>gzoltar.exec</code>.
   * 
   * @param file Location to write coverage execution data to
   */
  public void setDestfile(final File file) {
    this.destfile = file;
  }

  /**
   * Sets the location from where classes could be instrumented. Classes that are not under
   * <code>classesDirectory</code> will not be instrumented.
   * 
   * @param buildLocation Location with classes to instrument
   */
  public void setBuildLocation(final String buildLocation) {
    this.agentConfigs.setBuildLocation(buildLocation);
  }

  /**
   * List of wildcard patterns classes to include for instrumentation. Default is <code>*</code>
   * 
   * @param includes Wildcard pattern of included classes
   */
  public void setIncludes(final String includes) {
    this.agentConfigs.setIncludes(includes);
  }

  /**
   * List of wildcard patterns classes to exclude from instrumentation. Default is the empty string,
   * no classes excluded
   * 
   * @param excludes Wildcard pattern of excluded classes
   */
  public void setExcludes(final String excludes) {
    this.agentConfigs.setExcludes(excludes);
  }

  /**
   * List of wildcard patterns for classloaders that GZoltar will not instrument classes from.
   * Default is <code>sun.reflect.DelegatingClassLoader</code>
   * 
   * @param exclClassLoader Wildcard pattern of class loaders to exclude
   */
  public void setExclClassLoader(final String exclClassLoader) {
    this.agentConfigs.setExclClassloader(exclClassLoader);
  }

  /**
   * Sets whether classes without source location should be instrumented. Default is
   * <code>false</code>.
   * 
   * @param inclNoLocationClasses <code>true</code> if classes without source location should be instrumented
   */
  public void setInclNoLocationClasses(final boolean inclNoLocationClasses) {
    this.agentConfigs.setInclNoLocationClasses(inclNoLocationClasses);
  }

  /**
   * Sets the output method. Default is <code>console</code>
   * 
   * @param output Output method
   */
  public void setOutput(final String output) {
    this.agentConfigs.setOutput(output);
  }

  /**
   * Sets the granularity level of instrumentation. Default is <code>line</code>.
   * 
   * @param granularity Granularity level
   */
  public void setGranularity(final String granularity) {
    this.agentConfigs.setGranularity(granularity);
  }

  /**
   * Sets whether public methods should be instrumented. Default is <code>true</code>.
   * 
   * @param inclPublicMethods <code>true</code> if public methods should be instrumented
   */
  public void setInclPublicMethods(final boolean inclPublicMethods) {
    this.agentConfigs.setInclPublicMethods(inclPublicMethods);
  }

  /**
   * Sets whether public static constructors should be instrumented. Default is <code>false</code>.
   * 
   * @param inclStaticConstructors <code>true</code> if public static constructors should be
   *        instrumented
   */
  public void setInclStaticConstructors(final boolean inclStaticConstructors) {
    this.agentConfigs.setInclStaticConstructors(inclStaticConstructors);
  }

  /**
   * Sets whether deprecated methods should be instrumented. Default is <code>true</code>.
   * 
   * @param inclDeprecatedMethods <code>true</code> if deprecated methods should be instrumented
   */
  public void setInclDeprecatedMethods(final boolean inclDeprecatedMethods) {
    this.agentConfigs.setInclDeprecatedMethods(inclDeprecatedMethods);
  }

  /**
   * Creates JVM argument to launch with the specified GZoltar agent jar and the current options
   * 
   * @return JVM Argument to pass to new VM
   */
  protected String getLaunchingArgument() {
    return this.prepareAgentOptions().getVMArgument(getAgentFile());
  }

  private AgentConfigs prepareAgentOptions() {
    if (AgentOutput.FILE.equals(this.agentConfigs.getOutput())) {
      this.agentConfigs.setDestfile(this.destfile.getAbsolutePath());
    }
    return this.agentConfigs;
  }

  private File getAgentFile() {
    try {
      File agentFile = null;
      final String agentFileLocation = getProject().getProperty("_gzoltar.agentFile");
      if (agentFileLocation != null) {
        agentFile = new File(agentFileLocation);
      } else {
        agentFile = AgentJar.extractToTempLocation();
        getProject().setProperty("_gzoltar.agentFile", agentFile.toString());
      }

      return agentFile;
    } catch (final IOException e) {
      throw new BuildException("Unable to extract agent jar", e, getLocation());
    }
  }
}