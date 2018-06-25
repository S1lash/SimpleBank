# SimpleBank app #


Operation Controller
--------------------
отвечает за выполнение операций

| Название | Описание |
| transfer | перевод средств со счета на счет |
| deposit | внесение средств на счет |
| withdrawal | снятие средств со счета |

method: /operation/transfer
---------------------------
request body (required = true):

| Название | Тип | Обязателен | Описание |
| amount | long | да | сумма перевода в копейках |
| debitPart | object : AccountInfo (см. ниже) | да | Счет куда зачисляются средства. Должен содержать или id клиента, или номер счета |
| creditPart | object : AccountInfo (см. ниже) | да | Счет откуда списываются средства. Должен содержать или id клиента, или номер счета |

Пример запроса: 
{
	"amount" : "1234",
	"debitPart" : {
		"accountNumber" : "0000000000000000004"
	},
	"creditPart" : {
		"accountNumber" : "0000000000000000006"
	}
}

Пример ответа:
{
	"error": false,
	"transactionId": 5,
	"transactionType": "TRANSFER",
	"state": "APPROVED",
	"description": null,
	"status": "SUCCESS"
}

method: /operation/withdrawal
-----------------------------
request body (required = true):

| Название | Тип | Обязателен | Описание |
| amount | long | да | сумма перевода в копейках |
| debitPart | object : AccountInfo (см. ниже) | да | Счет куда зачисляются средства. Должен содержать id терминала |
| creditPart | object : AccountInfo (см. ниже) | да | Счет откуда списываются средства. Должен содержать или id клиента, или номер счета |

Логика по обналичиванию средств из терминала не реализована.

Пример запроса: 
{
	"amount" : "60000",
	"debitPart" : {
		"terminalId" : "1"
	},
	"creditPart" : {
		"accountNumber" : "0000000000000000004"
	}
}

Пример ответа:
{
	"error": false,
	"transactionId": 8,
	"transactionType": "WITHDRAWAL",
	"state": "APPROVED",
	"description": null,
	"status": "SUCCESS"
}

method: /operation/deposit
-----------------------------
request body (required = true):

| Название | Тип | Обязателен | Описание |
| amount | long | да | сумма перевода в копейках |
| debitPart | object : AccountInfo (см. ниже) | да | Счет куда зачисляются средства. Должен содержать или id клиента, или номер счета |
| creditPart | object : AccountInfo (см. ниже) | да | Счет откуда списываются средства. Должен содержать id терминала |

Логика по внесению средств в терминал не реализована.

Пример запроса: 
{
	"amount" : "60000",
	"debitPart" : {
		"accountNumber" : "0000000000000000004"
	},
	"creditPart" : {
		"terminalId" : "1"
	}
}

Пример ответа:
{
	"error": false,
	"transactionId": 9,
	"transactionType": "DEPOSIT",
	"state": "APPROVED",
	"description": null,
	"status": "SUCCESS"
}

Пример ответа с ошибкой валидации
---------------------------------
{
	"error": true,
	"errorMessage": "[DebitPart] must contain [clientId] or [accountNumber]",
	"cause": "validation.error",
	"status": "FAIL"
}

object : AccountInfo
---------------------
| Название | Тип | Обязателен | Описание |
| clientId | long| нет | Идентификатор клиента |
| accountNumber | String (1..19)| нет | Номер счета клиента |
| terminalId | long | нет | Идентификатор терминала |





Info Controller 
---------------
носит вспомогательный характер для отображения существующих данных

method: /info/accounts/ 
-----------------------
request body (required = false):

| Название | Тип | Обязателен | Описание |
| ids | List<Long> | нет | содержит список ID интересующих счетов (пустой список = "найти все") |

неуказанное тело запроса означает "найти все"

Пример запроса: 
{
	"ids" : ["1", "2", "11111"]
}

Пример ответа:
{
	"accounts": [
		{
			"id": 1,
			"balance": 99999999999999,
			"pan": null,
			"createdDate": "2018-06-25T10:35:37.469+0000",
			"number": "0000000000000000000",
			"enable": true,
			"ownerId": 1
		},
		{
			"id": 2,
			"balance": 910000,
			"pan": null,
			"createdDate": "2018-06-25T10:35:37.469+0000",
			"number": "0000000000000000002",
			"enable": false,
			"ownerId": 2
		}
	],
	"status": "SUCCESS"
}

method: /info/accountOwners/ 
----------------------------
request body (required = false):

| Название | Тип | Обязателен | Описание |
| ids | List<Long> | нет | содержит список ID интересующих владельцев счетов (пустой список = "найти все") |

неуказанное тело запроса означает "найти все"

Пример запроса: 
{
	"ids" : ["1", "2", "11111"]
}

Пример ответа:
{
	"accountOwners": [
		{
			"id": 1,
			"fullName": "OWNER NAME-1",
			"type": "BANK",
			"createdDate": "2018-06-25T10:35:37.464+0000",
			"enable": true
		},
		{
			"id": 2,
			"fullName": "OWNER NAME-2",
			"type": "INDIVIDUAL",
			"createdDate": "2018-06-25T10:35:37.464+0000",
			"enable": true
		}
	],
	"status": "SUCCESS"
}

method: /info/transactions/ 
---------------------------
request body (required = false):

| Название | Тип | Обязателен | Описание |
| ids | List<Long> | нет | содержит список ID интересующих транзакций (пустой список = "найти все") |

неуказанное тело запроса означает "найти все"

Пример запроса: 
{
	"ids" : ["1", "2", "11111"]
}

Пример ответа:
{
	"transactions": [
		{
			"id": 1,
			"state": "APPROVED",
			"amount": 234000,
			"createdDate": "2018-06-25T10:35:37.473+0000",
			"debitAccountId": 4,
			"creditAccountId": 1,
			"type": "DEPOSIT"
		},
		{
			"id": 2,
			"state": "APPROVED",
			"amount": 349003,
			"createdDate": "2018-06-25T10:35:37.473+0000",
			"debitAccountId": 4,
			"creditAccountId": 5,
			"type": "TRANSFER"
		}
	],
	"status": "SUCCESS"
}

method: /info/terminals/ 
------------------------
request body (required = false):

| Название | Тип | Обязателен | Описание |
| ids | List<Long> | нет | содержит список ID интересующих терминалов (пустой список = "найти все") |

неуказанное тело запроса означает "найти все"

Пример запроса: 
{
	"ids" : ["1", "2", "11111"]
}

Пример ответа:
{
	"terminals": [
		{
			"id": 1,
			"address": "Moscow, Red Square, 1",
			"type": "ATM",
			"createdDate": "2018-06-25T10:35:37.472+0000",
			"enable": true,
			"accountId": 1
		},
		{
			"id": 2,
			"address": "Moscow, Red Square, 1",
			"type": "POS",
			"createdDate": "2018-06-25T10:35:37.472+0000",
			"enable": true,
			"accountId": 1
		}
	],
	"status": "SUCCESS"
}
