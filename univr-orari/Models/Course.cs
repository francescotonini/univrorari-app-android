﻿// The MIT License (MIT)
// 
// Copyright (c) 2017-2017 Francesco Tonini <francescoantoniotonini@gmail.com>
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
// documentation files (the "Software"), to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
// to permit persons to whom the Software is furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all copies or substantial portions
// of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
// BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

#region

using System.Collections.Generic;
using Newtonsoft.Json;

#endregion

namespace univr_orari.Models
{
	public class AcademicYear
	{
		/// <summary>
		///     Academic year id
		/// </summary>
		[JsonProperty("valore")]
		public string Id { get; set; }

		/// <summary>
		///     Academic year courses
		/// </summary>
		[JsonProperty("elenco")]
		public List<Course> Courses { get; set; }
	}

	public class Course
	{
		/// <summary>
		///     Course name
		/// </summary>
		[JsonProperty("label")]
		public string Label { get; set; }

		/// <summary>
		///     Course value
		/// </summary>
		[JsonProperty("valore")]
		public string Value { get; set; }

		/// <summary>
		///     Course years
		/// </summary>
		[JsonProperty("elenco_anni")]
		public List<CourseYear> Years { get; set; }
	}

	public class CourseYear
	{
		/// <summary>
		///     Course name
		/// </summary>
		[JsonProperty("label")]
		public string Label { get; set; }

		/// <summary>
		///     Course id
		/// </summary>
		[JsonProperty("valore")]
		public string Value { get; set; }
	}
}