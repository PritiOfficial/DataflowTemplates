/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.dataflow.cdc.applier;

import java.util.Map;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollectionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeySchemasByTableFn extends DoFn<Long, KV<String, KV<Schema, Schema>>> {
  private static final Logger LOG = LoggerFactory.getLogger(KeySchemasByTableFn.class);

  final PCollectionView<Map<String, KV<Schema, Schema>>> tablesInput;

  KeySchemasByTableFn(
      PCollectionView<Map<String, KV<Schema, Schema>>> tablesInput) {
    this.tablesInput = tablesInput;
  }

  @ProcessElement
  public void processElement(ProcessContext c) {
    Map<String, KV<Schema, Schema>> tablesMap = c.sideInput(tablesInput);

    for(Map.Entry<String, KV<Schema, Schema>> tableAndSchemas : tablesMap.entrySet()) {
      c.output(KV.of(tableAndSchemas.getKey(), tableAndSchemas.getValue()));

    }
  }
}
