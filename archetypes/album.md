---
title: "{{ replace .File.ContentBaseName "-" " " | title }}"
date: {{ .Date }}
description: ""
categories: []
keywords: []
params:
  sort_by: "Params.weight"
  sort_order: "asc"
  theme: "dark"
resources:
  - src: feature.jpg
    params:
      cover: true
      weight: 10
---

写下这组照片的拍摄背景、路线、光线、器材或后期思路。
