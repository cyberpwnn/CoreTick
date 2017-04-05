package coretick.util;

public interface Configurable
{
	void onNewConfig(ClusterConfig cc);
	void onReadConfig();
	ClusterConfig getConfiguration();
	String getCodeName();
}
