package br.com.api.desafio.Enums;

import java.util.Set;

public class DepartmentRules {
    public static Set<String> getAllowedModules(Departament dept) {
        return switch (dept) {
            case TI -> Set.of("Financeiro", "Relatórios", "Portal", "RH", "Estoque",
                    "Compras", "Aprovador Financeiro", "Solicitante Financeiro", "Administrador RH",
                    "Colaborador RH");

            case FINANCEIRO -> Set.of("Financeiro", "Relatórios", "Portal");

            case RH -> Set.of("RH", "Relatórios", "Portal");

            case OPERACOES -> Set.of("Estoque", "Compras", "Relatórios", "Portal");

            default -> Set.of("Portal", "Relatórios");
        };
    }
}
