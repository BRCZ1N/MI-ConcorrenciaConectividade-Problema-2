package utilityclasses;
/**
A enumeração BatteryLevel representa os possíveis níveis de bateria de um dispositivo.
Esta enumeração contém os seguintes níveis de bateria:
DEFAULT: o nível padrão de bateria com um valor de 100
MEDIUM: um nível médio de bateria com um valor de 50
LOW: um nível baixo de bateria com um valor de 25
*/
public enum BatteryLevel {

	DEFAULT(100), MEDIUM(50), LOW(25) , DISCHARGED(0);

	private int batteryLevel;
	
	
	
	/**
	 * Constrói um objeto BatteryLevel com o nível de bateria especificado.
	 * 
	 * @param batteryLevel - um valor inteiro representando o nível de bateria
	 */
	private BatteryLevel(int batteryLevel) {

		this.batteryLevel = batteryLevel;
		
	}
	/**
	 * Retorna o valor inteiro representando o nível de bateria.
	 * 
	 * @return um valor inteiro representando o nível de bateria
	 */
	public int getBatteryLevel() {
		return batteryLevel;
	}
	/**
	 * Define o valor inteiro representando o nível de bateria.
	 * 
	 * @param batteryLevel - um valor inteiro representando o nível de bateria a ser definido
	 */
	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

}
