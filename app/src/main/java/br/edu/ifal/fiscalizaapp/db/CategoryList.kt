package br.edu.ifal.fiscalizaapp.db

import br.edu.ifal.fiscalizaapp.R
import br.edu.ifal.fiscalizaapp.model.CategoryEntity

val categoryList = listOf (
    CategoryEntity(
        iconResId = R.drawable.ic_poste,
        title = "Iluminação",
        description = "Postes apagados, lâmpadas queimadas ou piscando, e problemas na iluminação de praças e parques.",
    ),
    CategoryEntity(
        iconResId = R.drawable.ic_pavimentacao,
        title = "Pavimentação",
        description = "Buracos, asfalto irregular, lajotas soltas ou problemas de drenagem em ruas e avenidas."
    ),
    CategoryEntity(
        iconResId = R.drawable.ic_saneamento,
        title = "Saneamento",
        description = "Vazamentos de água ou esgoto na rua, bueiros entupidos ou falta de coleta de esgoto.",
    ),
    CategoryEntity(
        iconResId = R.drawable.ic_orgaos_publicos,
        title = "Órgãos públicos",
        description = "Problemas no atendimento ou na infraestrutura de prédios da prefeitura, postos de saúde e escolas.",
    ),
    CategoryEntity(
        iconResId = R.drawable.ic_transporte_coletivo,
        title = "Transporte coletivo",
        description = "Atrasos, superlotação, estado de conservação dos ônibus e problemas em pontos de parada.",
    ),
    CategoryEntity(
        iconResId = R.drawable.ic_espaco_publico,
        title = "Espaço público",
        description = "Manutenção de praças, parques, calçadas, terrenos baldios e outras áreas de uso comum.",
    ),
    CategoryEntity(
        iconResId = R.drawable.ic_semaforo_apagado,
        title = "Trânsito",
        description = "Semáforos com defeito, falta de sinalização, ou problemas na organização do tráfego de veículos.",
    ),
    CategoryEntity(
        iconResId = R.drawable.ic_outras_categorias,
        title = "Outros",
        description = "Não encontrou o seu problema? Descreva sua solicitação nesta categoria.",
    )
)