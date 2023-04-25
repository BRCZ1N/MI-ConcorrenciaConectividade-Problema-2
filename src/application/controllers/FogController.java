/**

Esta classe é responsável por definir os endpoints para as requisições relacionadas às estações de carregamento de veículos elétricos.
Os endpoints implementam os métodos HTTP GET para retornar informações sobre a estação de carregamento, como a fila mais curta, a melhor localização e todas as estações disponíveis.
Os métodos desta classe utilizam a classe ChargingStationService para acessar os dados das estações de carregamento e retornar as informações solicitadas em formato JSON.
*/
package application.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import application.model.FogModel;
import application.services.FogService;

@RestController
@ComponentScan("application.services")
@RequestMapping("/fog")
public class FogController {

	@Autowired
	private static FogService fogService;
	/**
	 * Método para obter a instância do serviço FogService.
	 * 
	 * @return a instância do FogService.
	 */
	public static FogService FogService() {
		
		return fogService;
		
	}
	/**
	 * Método para adicionar uma estação de carregamento.
	 * 
	 * @param station a estação de carregamento a ser adicionada.
	 */
	public static void addFog(FogModel fog) {
	
		FogService.addFog(fog);
	
	}

/**
 * Endpoint para obter todas as estações de carregamento disponíveis.
 * 
 * @return uma HttpEntity contendo as informações de todas as estações de carregamento em formato JSON.
 */
	@GetMapping("/all")
	public HttpEntity<String> getBestStationsOtherRegions() {

		return FogService.getOrdersListAllRegionsForQueue().map(stations -> ResponseEntity.ok(stations.toString())).orElse(ResponseEntity.notFound().build());

	}

}
