/**
 * Copyright (c) 2015-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @providesModule convertRequestBody
 * @flow
 * @format
 */
'use strict';

const binaryToBase64 = require('../Utilities/binaryToBase64');

const Blob = require('../Blob/Blob');
const FormData = require('./FormData');

export type RequestBody =
  | string
  | Blob
  | FormData
  | {uri: string}
  | ArrayBuffer
  | $ArrayBufferView;

function convertRequestBody(body: RequestBody): Object {
  if (typeof body === 'string') {
    return {string: body};
  }
  if (body instanceof Blob) {
    return {blob: body.data};
  }
  if (body instanceof FormData) {
    return {formData: body.getParts()};
  }
  if (body instanceof ArrayBuffer || ArrayBuffer.isView(body)) {
    // $FlowFixMe: no way to assert that 'body' is indeed an ArrayBufferView
    return {base64: binaryToBase64(body)};
  }
  return body;
}

module.exports = convertRequestBody;