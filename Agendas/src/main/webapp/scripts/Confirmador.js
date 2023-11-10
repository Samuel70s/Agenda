/**
 *Confirmação de exclusão de um contato
 @author Samuel  
 */
function confirmar(idcon){
	let resposta = confirm("Confirma a exclusão desse contato?")
	if(resposta==true){
		
		//Teste para confirmação de recebimento do idcon
		//alert(idcon);
		
		window.location.href="delete?idcon="+idcon
	}
}