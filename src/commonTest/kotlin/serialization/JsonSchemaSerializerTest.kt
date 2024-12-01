package com.xemantic.ai.tool.schema.serialization

import com.xemantic.ai.tool.schema.JsonSchema
import com.xemantic.ai.tool.schema.test.testJson
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import kotlin.test.Test

class JsonSchemaSerializerTest {

  @Test
  fun `should decode JsonSchema reference`() {
    val json = $$"""
      {
        "$ref": "#/definitions/foo"
      }
    """.trimIndent()
    val ref = testJson.decodeFromString<JsonSchema>(json)
    ref shouldBe instanceOf<JsonSchema.Ref>()
    (ref as JsonSchema.Ref).ref shouldBe "#/definitions/foo"
  }

  @Test
  fun `decode JSON Schema from JSON`() {
    val json = $$"""
      {
        "type": "object",
        "description": "Personal data",
        "properties": {
          "name": {
            "type": "string",
            "description": "The official name"
          },
          "birthDate": {
            "type": "string",
            "format": "date-time"
          },
          "email": {
            "type": "string",
            "minLength": 6,
            "maxLength": 100,
            "format": "email"
          },
          "address": {
            "$ref": "#/definitions/address"
          },
          "hobbies": {
            "type": "array",
            "description": "A list of hobbies of the person",
            "items": {
              "type": "string",
              "title": "A hobby item",
              "description": "A hobby must be a unique identifier consisting out of lower case letters and underscores",
              "pattern": "[a-z_]"
            },
            "minItems": 0,
            "maxItems": 10,
            "uniqueItems": true
          },
          "mentors": {
            "type": "array",
            "items": {
              "$ref": "#/definitions/mentor"
            }
          },
          "salary": {
            "type": "string",
            "description": "A monetary amount",
            "pattern": "^-?[0-9]+\\.[0-9]{2}?$"
          },
          "tax": {
            "type": "string",
            "pattern": "^-?\\d+(\\.\\d+)?$"
          },
          "status": {
            "type": "string",
            "title": "Entry status",
            "description": "The enumeration of possible entry status states, e.g. 'verification-pending', 'verified'",
            "enum": [
              "verification-pending",
              "verified"
            ]
          },
          "avatar": {
            "type": "string",
            "contentEncoding": "base64",
            "contentMediaType": "image/png"
          },
          "tokens": {
            "type": "integer",
            "minimum": 0,
            "maximum": 1000
          },
          "karma": {
            "type": "integer",
            "exclusiveMinimum": 0,
            "exclusiveMaximum": 100
          },
          "experience": {
            "type": "number",
            "minimum": 0.0,
            "maximum": 100.0
          },
          "factor": {
            "type": "number",
            "exclusiveMinimum": 0.0,
            "exclusiveMaximum": 1.0
          }
        },
        "required": [
          "name",
          "birthDate",
          "address",
          "salary",
          "tax",
          "status",
          "avatar",
          "tokens",
          "karma",
          "experience",
          "factor"
        ],
        "definitions": {
          "address": {
            "type": "object",
            "title": "The full address",
            "description": "An address of a person or an organization",
            "properties": {
              "street": {
                "type": "string"
              },
              "city": {
                "type": "string"
              },
              "postalCode": {
                "type": "string",
                "description": "A postal code not limited to particular country",
                "minLength": 3,
                "maxLength": 10
              },
              "countryCode": {
                "type": "string",
                "pattern": "[a-z]{2}"
              }
            },
            "required": [
              "street",
              "city",
              "postalCode",
              "countryCode"
            ]
          },
          "mentor": {
            "type": "object",
            "properties": {
              "id": {
                "type": "string"
              }
            },
            "required": [
              "id"
            ]
          }
        }
      }
    """
    val schema = testJson.decodeFromString<JsonSchema>(json)
    schema.toString() shouldEqualJson json
  }

}
