package br.com.fiap.incidentmanagement.dto;

import java.util.List;

public record SummaryData(
        Long active,
        Long waiting,
        Long progressing,
        Long solved,
        Long unsolved,
        Long closed,
        List<KeyValueData> byCategory,
        List<KeyValueData> byType,
        List<KeyValueData> bySchooclass,
        List<KeyValueData> byStudent,
        List<KeyValueData> byPeriod) {
}
