package com.example.telaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AzulPrincipal = Color(0xFF2255F5)
private val AzulClaroFundo = Color(0xFFF3F4F8)
private val CinzaTexto = Color(0xFF8A8F98)
private val VerdeValor = Color(0xFF17A673)
private val CinzaBotaoDesabilitado = Color(0xFFB7BDC9)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PaymentScreen()
            }
        }
    }
}

enum class FormaPagamento {
    CARTAO_CREDITO,
    PIX
}

@Composable
fun PaymentScreen() {
    var formaPagamentoSelecionada by remember { mutableStateOf(FormaPagamento.CARTAO_CREDITO) }

    var numeroCartao by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var nomeTitular by remember { mutableStateOf("") }

    var pagamentoConfirmado by remember { mutableStateOf(false) }

    val nomeCarro = "Ford Focus 2023"
    val periodoReserva = "15 Out - 20 Out (5 Diárias)"
    val valorTotal = "R$ 600,00"

    val formularioValido = isFormularioValido(numeroCartao, validade, cvv, nomeTitular)

    Scaffold(
        topBar = { PaymentTopBar() },
        containerColor = AzulClaroFundo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AzulClaroFundo)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            ResumoDaReserva(
                nomeCarro = nomeCarro,
                periodo = periodoReserva,
                valorTotal = valorTotal
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Forma de Pagamento",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            FormasDePagamento(
                selecionada = formaPagamentoSelecionada,
                onSelecionar = { formaPagamentoSelecionada = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            CamposDoCartao(
                numeroCartao = numeroCartao,
                onNumeroCartaoChange = { numeroCartao = it },
                validade = validade,
                onValidadeChange = { validade = it },
                cvv = cvv,
                onCvvChange = { cvv = it },
                nomeTitular = nomeTitular,
                onNomeTitularChange = { nomeTitular = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            BotaoConfirmarPagamento(
                habilitado = formularioValido,
                onConfirmar = { pagamentoConfirmado = true }
            )

            if (pagamentoConfirmado) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Pagamento concluído",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeValor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Pagamento",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun ResumoDaReserva(nomeCarro: String, periodo: String, valorTotal: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "RESUMO DA RESERVA",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CinzaTexto
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(
                    text = nomeCarro,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = periodo,
                    fontSize = 13.sp,
                    color = CinzaTexto
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = AzulClaroFundo, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Valor Total", fontSize = 13.sp, color = CinzaTexto)
                Text(
                    text = valorTotal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeValor
                )
            }
        }
    }
}

@Composable
fun FormasDePagamento(
    selecionada: FormaPagamento,
    onSelecionar: (FormaPagamento) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OpcaoDePagamento(
            titulo = "Cartão de Crédito",
            subtitulo = null,
            selecionada = selecionada == FormaPagamento.CARTAO_CREDITO,
            onClick = { onSelecionar(FormaPagamento.CARTAO_CREDITO) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        OpcaoDePagamento(
            titulo = "Pix",
            subtitulo = "Desconto de 5%",
            selecionada = selecionada == FormaPagamento.PIX,
            onClick = { onSelecionar(FormaPagamento.PIX) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun OpcaoDePagamento(
    titulo: String,
    subtitulo: String?,
    selecionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(
                width = if (selecionada) 2.dp else 1.dp,
                color = if (selecionada) AzulPrincipal else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickableSemTiraEfeitoVisual(onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                if (subtitulo != null) {
                    Text(text = subtitulo, fontSize = 11.sp, color = CinzaTexto)
                }
            }
            RadioButton(
                selected = selecionada,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = AzulPrincipal)
            )
        }
    }
}

@Composable
fun CamposDoCartao(
    numeroCartao: String,
    onNumeroCartaoChange: (String) -> Unit,
    validade: String,
    onValidadeChange: (String) -> Unit,
    cvv: String,
    onCvvChange: (String) -> Unit,
    nomeTitular: String,
    onNomeTitularChange: (String) -> Unit
) {
    Column {
        LabelDoCampo("NÚMERO DO CARTÃO")
        OutlinedTextField(
            value = numeroCartao,
            onValueChange = { novoValor -> onNumeroCartaoChange(novoValor) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0000 0000 0000 0000") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                LabelDoCampo("VALIDADE")
                OutlinedTextField(
                    value = validade,
                    onValueChange = { novoValor -> onValidadeChange(formatarValidade(novoValor)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("MM/AA") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                LabelDoCampo("CVV")
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { novoValor -> onCvvChange(novoValor) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("•••") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LabelDoCampo("NOME DO TITULAR")
        OutlinedTextField(
            value = nomeTitular,
            onValueChange = { novoValor -> onNomeTitularChange(novoValor.uppercase()) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("NOME COMO ESTÁ NO CARTÃO") },
            singleLine = true,
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
fun LabelDoCampo(texto: String) {
    Text(
        text = texto,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = CinzaTexto
    )
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun BotaoConfirmarPagamento(habilitado: Boolean, onConfirmar: () -> Unit) {
    Button(
        onClick = { onConfirmar() },
        enabled = habilitado,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrincipal,
            disabledContainerColor = CinzaBotaoDesabilitado
        )
    ) {
        Text(
            text = "CONFIRMAR PAGAMENTO",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

fun formatarValidade(entrada: String): String {
    val apenasDigitos = entrada.filter { caractere -> caractere.isDigit() }.take(4)

    return if (apenasDigitos.length > 2) {
        val mes = apenasDigitos.substring(0, 2)
        val ano = apenasDigitos.substring(2)
        "$mes/$ano"
    } else {
        apenasDigitos
    }
}

fun isNumeroCartaoValido(numero: String): Boolean {
    val apenasNumeros = numero.replace(" ", "")
    if (apenasNumeros.length != 16) return false
    return apenasNumeros.all { caractere -> caractere.isDigit() }
}

fun isValidadeValida(validade: String): Boolean {
    val partes = validade.split("/")
    if (partes.size != 2) return false

    val mes = partes[0]
    val ano = partes[1]

    if (mes.length != 2 || ano.length != 2) return false
    if (!mes.all { it.isDigit() } || !ano.all { it.isDigit() }) return false

    val mesNumero = mes.toInt()
    return mesNumero in 1..12
}

fun isCvvValido(cvv: String): Boolean {
    if (cvv.length != 3) return false
    return cvv.all { caractere -> caractere.isDigit() }
}

fun isNomeTitularValido(nome: String): Boolean {
    return nome.trim().isNotEmpty()
}

fun isFormularioValido(
    numeroCartao: String,
    validade: String,
    cvv: String,
    nomeTitular: String
): Boolean {
    return isNumeroCartaoValido(numeroCartao) &&
            isValidadeValida(validade) &&
            isCvvValido(cvv) &&
            isNomeTitularValido(nomeTitular)
}

private fun Modifier.clickableSemTiraEfeitoVisual(onClick: () -> Unit): Modifier =
    this.then(
        Modifier.clickable(onClick = onClick)
    )

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    MaterialTheme {
        PaymentScreen()
    }
}